package com.vectorization.core.collection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;

public abstract class FileCompositeCollection extends AbstractVectorCollection {
	
	public static final int MAX_SIZE = 100000;
	
	private String filename;
	private List<File> subCollections;
	private VectorCollection currentCollection;
	private int currentFile = -1;
	
	public FileCompositeCollection(int dimensionality, String filename, List<File> subCollections) {
		super(dimensionality);
		this.filename = filename;
		this.subCollections = subCollections;
		loadNext();
	}

	public VectorCollection retrieveKnn(int k, Vector prototype) {
		VectorCollection result = currentCollection.retrieveKnn(k, prototype);
		for(int i = 0; i < subCollections.size() - 1; i++){
			loadNext();
			result.insertAll(currentCollection.retrieveKnn(k, prototype));
		}
		return result;
	}
	
	@Override
	public VectorCollection removeAll(String... vectorNames) {
		currentCollection.removeAll(vectorNames);
		for(int i = 0; i < subCollections.size() - 1; i++){
			loadNext();
			currentCollection.removeAll(vectorNames);
		}
		return this;
	}
	
	public abstract VectorCollection newCollection();
	
	@Override
	public VectorCollection insertAll(List<Vector> myObjects) {
		int from = 0;
		for(int i = 0; i < subCollections.size(); i++){
			if (from >= myObjects.size())
				break;
			int remaining = MAX_SIZE - currentCollection.size();
			int to = from + remaining;
			if (to >= myObjects.size()) {
				to = myObjects.size();
			}
			currentCollection.insertAll(myObjects.subList(from, to));
			from = to + 1;
			loadNext();
		}

		while (from < myObjects.size()) {
			VectorCollection sub = newCollection();
			int to = from + MAX_SIZE;
			if (to >= myObjects.size()) {
				to = myObjects.size();
			}
			sub.insertAll(myObjects.subList(from, to));
			
			try {
				sub.save(addFileExtention(filename)+ "_" + (subCollections.size() + 1));
				subCollections.add(new File(filename));
			} catch (IOException e) {
				throw new VectorizationException(e);
			}
			from = to + 1;
		}
		return this;
	}
	
	@Override
	public void save(String filename) throws IOException {
		currentCollection.save(addFileExtention(filename) + "_" + currentFile);
	}

	@Override
	public String addFileExtention(String string) {
		return string + ".part";
	}
	
	private void loadNext(){
		try{
			currentFile = (currentFile + 1) % subCollections.size();
			File f = subCollections.get(currentFile);
			currentCollection = load(f);
		}catch(Exception e){
			
		}
	}
	
	private VectorCollection load(File file)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			VectorCollection collection = (VectorCollection) ois.readObject();
			return collection;
		} finally {
			if (ois != null)
				ois.close();
		}
	}

}
