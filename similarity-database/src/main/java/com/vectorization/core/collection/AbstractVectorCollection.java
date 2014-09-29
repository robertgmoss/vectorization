/*  
 *  Copyright (C) 2014 Robert Moss
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.vectorization.core.collection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;

/**
 * Provides a simple implementation that allows extension by overriding the
 * default methods. At the same time, provides a simple mapping to allow vectors
 * to be reference by their identifiers. Overriding classes should ensure that a
 * call to super in the insert method is made to maintain this mapping.
 * 
 * @author Robert Moss
 *
 * @param <E>
 */
public abstract class AbstractVectorCollection implements VectorCollection {

	private static final long serialVersionUID = -1655815257490860488L;
	private Map<String, Vector> vectors = new LinkedHashMap<String, Vector>();
	private int dimensionality;

	public AbstractVectorCollection(int dimensionality) {
		this.dimensionality = dimensionality;
	}

	public final int dimensionality() {
		return dimensionality;
	}

	public final Vector get(String vectorName) {
		if (!vectors.containsKey(vectorName))
			throw new VectorizationException("");
		return vectors.get(vectorName);
	}

	private VectorCollection remove(String vectorName) {
		// if (!vectors.containsKey(vectorName))
		// throw new SSException("No such vector: " + vectorName);
		vectors.remove(vectorName);
		return this;
	}

	public VectorCollection removeAll(String... vectorNames) {
		for (String name : vectorNames) {
			this.remove(name);
		}
		return this;
	}

	private VectorCollection insert(Vector myObject) {
		// System.out.println("AbstractVector.insert");
		if (myObject.dimensionality() != this.dimensionality())
			throw new VectorizationException(
					"Cannot insert element with dimensionality: "
							+ myObject.dimensionality()
							+ " into collection with dimensionality: "
							+ this.dimensionality());
		vectors.put(myObject.id(), myObject);
		return this;
	}

	public VectorCollection insertAll(Vector... myObjects) {
		return insertAll(Arrays.asList(myObjects));
	}

	public VectorCollection insertAll(MetricSpace<Vector> myObjects) {
		List<Vector> values = myObjects.values();
		return insertAll(values);
	}

	public VectorCollection insertAll(List<Vector> myObjects) {
		for (Vector e : myObjects) {
			this.insert(e);
		}
		return this;
	}

	public final Iterator<Vector> iterator() {
		return vectors.values().iterator();
	}

	public final List<Vector> values() {
		return new ArrayList<Vector>(vectors.values());
	}

	public final boolean contains(Vector o) {
		return vectors.containsKey(o.id());
	}

	public final boolean contains(String id) {
		return vectors.containsKey(id);
	}

	public final int size() {
		return vectors.size();
	}

	public void save(String filename) throws IOException {
		ObjectOutputStream dos = null;
		try {
			dos = new ObjectOutputStream(new FileOutputStream(
					addFileExtention(filename)));
			dos.writeObject(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (dos != null)
				dos.close();
		}
	}

	public String addFileExtention(String string) {
		return string + ".db";
	}

	@Override
	public String toString() {
		if (size() == 0)
			return "empty";
		StringBuilder sb = new StringBuilder();
		Iterator<Vector> it = iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext())
				sb.append("\n");
		}
		return sb.toString();
	}
}
