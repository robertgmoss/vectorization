package com.vectorization.core.collection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;

public abstract class FileCompositeCollection implements Iterable<Vector>,
		Serializable {

	private static final long serialVersionUID = -3991632481923520975L;

	public static final int MAX_SIZE = 100000;

	private String database;
	private String mainSpaceName;

	private List<File> vectorSpaceFiles;

	private int dimensionality;

	class SpaceIterator implements Iterator<VectorSpace> {

		private Iterator<File> files;
		private String currentFileName;

		public SpaceIterator(Iterable<File> files) {
			this.files = files.iterator();
		}

		public boolean hasNext() {
			return files.hasNext();
		}

		public VectorSpace next() {
			if (!hasNext())
				throw new NoSuchElementException();
			try {
				File f = files.next();
				currentFileName = f.getName();
				return load(f);
			} catch (Exception e) {
				throw new VectorizationException(e);
			}
		}

		public String getCurrentFileName() {
			return currentFileName;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public FileCompositeCollection(int dimensionality, String database,
			String spaceName, List<File> vectorSpaceFiles) {
		this.dimensionality = dimensionality;
		this.database = database;
		this.mainSpaceName = spaceName;
		this.vectorSpaceFiles = vectorSpaceFiles;
	}

	public FileCompositeCollection(int dimensionality, String database,
			String filename) {
		this(dimensionality, database, filename, new ArrayList<File>());
	}

	public int dimensionality() {
		return dimensionality;
	}

	public Vector get(String vectorName) {
		for (Iterator<VectorSpace> i = new SpaceIterator(vectorSpaceFiles); i
				.hasNext();) {
			VectorSpace space = i.next();
			if (space.contains(vectorName)) {
				return space.get(vectorName);
			}
		}
		throw new VectorizationException("No vector " + vectorName);
	}

	public Iterable<Vector> retrieveKnn(int k, Vector prototype) {
		VectorSpace result = newCollection();
		for (Iterator<VectorSpace> i = new SpaceIterator(vectorSpaceFiles); i
				.hasNext();) {
			VectorSpace space = i.next();
			result.insertAll(space.retrieveKnn(k, prototype));
		}
		return result.retrieveKnn(k, prototype);
	}

	public FileCompositeCollection removeAll(String... vectorNames) {
		SpaceIterator i = new SpaceIterator(vectorSpaceFiles);
		while (i.hasNext()) {
			VectorSpace space = i.next();
			space.removeAll(vectorNames);
			try {
				String filename = i.getCurrentFileName();
				space.save(database + File.separator
						+ removeDbFileExtention(filename));
			} catch (IOException e) {
				throw new VectorizationException(e);
			}
		}
		return this;
	}

	public abstract VectorSpace newCollection();

	public FileCompositeCollection insertAll(MetricSpace<Vector> myObjects) {
		return insertAll(myObjects.values());
	}

	public FileCompositeCollection insertAll(Vector... myObjects) {
		return insertAll(Arrays.asList(myObjects));
	}

	public FileCompositeCollection insertAll(List<Vector> myObjects) {
		SpaceIterator i = new SpaceIterator(vectorSpaceFiles);
		List<String> names = new ArrayList<String>();
		for (Vector v : myObjects) {
			names.add(v.id());
		}
		while (i.hasNext()) {
			VectorSpace next = i.next();
			// we must remove old values from all files
			next.removeAll(names); 
			int stillToInsert = myObjects.size();
			if (stillToInsert == 0) continue;
			int remainingSpace = MAX_SIZE - next.size();
			int toInsert = stillToInsert > remainingSpace ? remainingSpace : stillToInsert;
			next.insertAll(myObjects.subList(0, toInsert));
			try {
				String filename = i.getCurrentFileName();
				next.save(database + File.separator
						+ removeDbFileExtention(filename));
			} catch (IOException e) {
				throw new VectorizationException(e);
			}
			if(toInsert == stillToInsert) myObjects = Collections.emptyList();
			else myObjects = myObjects.subList(toInsert, stillToInsert);
		}
		while (myObjects.size() > 0) {
			VectorSpace next = createNewSpaceAndFile();
			int stillToInsert = myObjects.size();
			int remainingSpace = MAX_SIZE - next.size();
			int toInsert = stillToInsert > remainingSpace ? remainingSpace : stillToInsert;
			next.insertAll(myObjects.subList(0, toInsert));
			try {
				String filename = getMostRecentFilename();
				next.save(database + File.separator
						+ removeDbFileExtention(filename));
			} catch (IOException e) {
				throw new VectorizationException(e);
			}
			if(toInsert == stillToInsert) myObjects = Collections.emptyList();
			else myObjects = myObjects.subList(toInsert, stillToInsert);
		}
		return this;

	}

	private String getMostRecentFilename() {
		return vectorSpaceFiles.get(
				vectorSpaceFiles.size() - 1).getName();
	}

	private String addFileExtention(int part) {
		return database + File.separator + mainSpaceName + ".part_" + part;
	}

	private String removeDbFileExtention(String filename) {
		return filename.substring(0, filename.lastIndexOf(".db"));
	}

	private VectorSpace createNewSpaceAndFile() {
		try {
			String f = addFileExtention(vectorSpaceFiles.size());
			VectorSpace newCollection = newCollection();
			newCollection.save(f);
			vectorSpaceFiles.add(new File(f + ".db"));
			return newCollection;
		} catch (IOException e) {
			throw new VectorizationException(e);
		}
	}

	public static VectorSpace load(File file) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		System.out.println("loading " + file.getName());
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			VectorSpace collection = (VectorSpace) ois.readObject();
			return collection;
		} finally {
			if (ois != null)
				ois.close();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		SpaceIterator i = new SpaceIterator(vectorSpaceFiles);
		while (i.hasNext()) {
			VectorSpace space = i.next();
			sb.append(space.toString());
		}
		return sb.toString();
	}

	public Iterator<Vector> iterator() {
		return new Iterator<Vector>() {

			Iterator<VectorSpace> spaces = new SpaceIterator(vectorSpaceFiles);
			Iterator<Vector> currentIterator;

			public boolean hasNext() {
				
				if(!currentIterator.hasNext() && !spaces.hasNext()){
					return false;
				}
				
				if(!currentIterator.hasNext()){
					currentIterator = spaces.next().iterator();
				}
				return currentIterator.hasNext();
			}

			public Vector next() {
				return currentIterator.next();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
