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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.vectorization.core.SSVector;

public abstract class CompositeCollection<E extends SSVector> extends
		AbstractCollection<E> {

	public static final int MAX_SIZE = 100000;
	private static final long serialVersionUID = 7922628750420516639L;
	private Collection<SSCollection<E>> subCollections = new LinkedList<SSCollection<E>>();

	public CompositeCollection(int dimensionality,
			Collection<SSCollection<E>> subCollections) {
		super(dimensionality);
		for (SSCollection<E> c : subCollections) {
			super.insertAll(c);
		}
	}

	public CompositeCollection(int dimensionality) {
		super(dimensionality);
	}

	public SSCollection<E> retrieveKnn(int k, E prototype) {
		SSCollection<E> result = new Space<E>(this.dimensionality());
		for (SSCollection<E> sub : subCollections) {
			result.insertAll(sub.retrieveKnn(k, prototype));
		}
		return result.retrieveKnn(k, prototype);
	}

	@Override
	public SSCollection<E> remove(String vectorName) {
		super.remove(vectorName);
		for (SSCollection<E> sub : subCollections) {
			sub.remove(vectorName);
		}
		return this;
	}

	@Override
	public SSCollection<E> removeAll(String... vectorNames) {
		super.removeAll(vectorNames);
		for (SSCollection<E> sub : subCollections) {
			sub.removeAll(vectorNames);
		}
		return this;
	}

	@Override
	public SSCollection<E> insert(E myObject) {
		super.insert(myObject);
		boolean inserted = false;
		for (SSCollection<E> sub : subCollections) {
			if (isFull(sub))
				continue;
			if (inserted)
				break;
			sub.insert(myObject);
			inserted = true;
		}

		if (!inserted) {
			SSCollection<E> col = newCollection();
			col.insert(myObject);
			subCollections.add(col);
		}

		return this;
	}

	@Override
	public SSCollection<E> insertAll(E... myObjects) {
		for (E e : myObjects) {
			super.insert(e);
		}
		int from = 0;
		for (SSCollection<E> sub : subCollections) {
			if (from >= myObjects.length)
				break;
			int remaining = MAX_SIZE - sub.size();
			int to = from + remaining;
			if (to >= myObjects.length) {
				to = myObjects.length;
			}
			sub.insertAll(Arrays.copyOfRange(myObjects, from, to));
			from = to + 1;
		}

		while (from < myObjects.length) {
			SSCollection<E> sub = newCollection();
			int to = from + MAX_SIZE;
			if (to >= myObjects.length) {
				to = myObjects.length;
			}
			sub.insertAll(Arrays.copyOfRange(myObjects, from, to));
			subCollections.add(sub);
			from = to + 1;
		}
		return this;
	}

	public abstract SSCollection<E> newCollection();

	@Override
	public void save(String filename) throws IOException {
		int i = 1;
		for (SSCollection<E> sub : subCollections) {
			sub.save(addFileExtention(filename) + "_" + i);
			i++;
		}
	}

	@Override
	public String addFileExtention(String string) {
		return string + ".part";
	}

	private boolean isFull(SSCollection<E> sub) {
		return sub.size() >= MAX_SIZE;
	}

}
