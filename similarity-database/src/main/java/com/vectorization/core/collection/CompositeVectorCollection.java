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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.vectorization.core.Vector;

public abstract class CompositeVectorCollection extends
		AbstractVectorCollection {

	public static final int MAX_SIZE = 100000;
	private static final long serialVersionUID = 7922628750420516639L;
	private Collection<VectorCollection> subCollections = new LinkedList<VectorCollection>();

	public CompositeVectorCollection(int dimensionality,
			Collection<VectorCollection> subCollections) {
		super(dimensionality);
		for (VectorCollection c : subCollections) {
			this.insertAll(c);
		}
	}

	public CompositeVectorCollection(int dimensionality) {
		super(dimensionality);
	}
	
	public Collection<VectorCollection> getSubCollections() {
		return subCollections;
	}

	public VectorCollection retrieveKnn(int k, Vector prototype) {
		
		VectorCollection result = new SimpleVectorSpace(this.dimensionality());
		for (VectorCollection sub : subCollections) {
//			System.out.println(sub.values());
			VectorCollection knn = sub.retrieveKnn(k, prototype);
//			System.out.println(sub.values());
			result.insertAll(knn);
		}
		return result.retrieveKnn(k, prototype);
	}

	@Override
	public VectorCollection removeAll(String... vectorNames) {
		super.removeAll(vectorNames);
		for (VectorCollection sub : subCollections) {
			sub.removeAll(vectorNames);
		}
		return this;
	}

	@Override
	public VectorCollection insertAll(List<Vector> myObjects) {
		
		super.insertAll(myObjects);
		int from = 0;
		for (VectorCollection sub : subCollections) {
			if (from >= myObjects.size())
				break;
			int remaining = MAX_SIZE - sub.size();
			int to = from + remaining;
			if (to >= myObjects.size()) {
				to = myObjects.size();
			}
			sub.insertAll(myObjects.subList(from, to));
			from = to + 1;
		}

		while (from < myObjects.size()) {
			VectorCollection sub = newCollection();
			int to = from + MAX_SIZE;
			if (to >= myObjects.size()) {
				to = myObjects.size();
			}
			sub.insertAll(myObjects.subList(from, to));
			subCollections.add(sub);
			from = to + 1;
		}
		return this;
	}
	
//	@Override
//	public VectorCollection insertAll(MetricSpace<Vector> myObjects) {
//		super.insertAll(myObjects);
//		return this;
//	}

	public abstract VectorCollection newCollection();

	@Override
	public void save(String filename) throws IOException {
		int i = 1;
		for (VectorCollection sub : subCollections) {
			sub.save(addFileExtention(filename) + "_" + i);
			i++;
		}
	}

	@Override
	public String addFileExtention(String string) {
		return string + ".part";
	}
}
