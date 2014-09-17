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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vectorization.core.SSException;
import com.vectorization.core.SSVector;

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
public abstract class AbstractCollection<E extends SSVector> implements
		SSCollection<E> {

	private static final long serialVersionUID = -1655815257490860488L;
	private Map<String, E> vectors = new LinkedHashMap<String, E>();
	private int dimensionality;

	public AbstractCollection(int dimensionality) {
		this.dimensionality = dimensionality;
	}

	public final int dimensionality() {
		return dimensionality;
	}

	public final SSVector get(String vectorName) {
		if (!vectors.containsKey(vectorName))
			throw new SSException("");
		return vectors.get(vectorName);
	}

	public SSCollection<E> remove(String vectorName) {
//		if (!vectors.containsKey(vectorName))
//			throw new SSException("No such vector: " + vectorName);
		vectors.remove(vectorName);
		return this;
	}
	
	public SSCollection<E> removeAll(String... vectorNames) {
		for(String name : vectorNames){
			this.remove(name);
		}
		return this;
	}

	public SSCollection<E> insert(E myObject) {
		if (myObject.dimensionality() != this.dimensionality())
			throw new SSException("Cannot insert element with dimensionality: "
					+ myObject.dimensionality()
					+ " into collection with dimensionality: "
					+ this.dimensionality());
		vectors.put(myObject.id(), myObject);
		return this;
	}

	public SSCollection<E> insertAll(E... myObjects) {
		for (E e : myObjects) {
			this.insert(e);
		}
		return this;
	}

	public SSCollection<E> insertAll(SSCollection<E> myObjects) {
		for (E e : myObjects) {
			this.insert(e);
		}
		return this;
	}

	public final Iterator<E> iterator() {
		return vectors.values().iterator();
	}

	public final Collection<E> values() {
		return vectors.values();
	}

	public final boolean contains(SSVector o) {
		return vectors.containsKey(o.id());
	}
	
	public final boolean contains(String id){
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
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext())
				sb.append("\n");
		}
		return sb.toString();
	}

}
