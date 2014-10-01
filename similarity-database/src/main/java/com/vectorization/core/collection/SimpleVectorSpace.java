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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vectorization.core.Vector;
import com.vectorization.core.vectors.Vectors;

/**
 * A simple implementation that uses the default methods of it superclass
 * to organise the data.  As a result the implementation of the retrieveKnn
 * method is naive whereby a sort is run every query.
 * 
 * @author Robert Moss
 *
 * @param <E>
 */
public class SimpleVectorSpace extends AbstractVectorSpace {

	private static final long serialVersionUID = 2751516171619962956L;
	
	public static SimpleVectorSpace parseSpace(String s){
		String[] lines = s.split("\n");
		SimpleVectorSpace space = null;
		for(String v : lines){
			String[] parts = v.split("=");
			Vector vector = Vectors.parseVector(parts[0].trim(), parts[1].trim());

			if(space == null){
				space = new SimpleVectorSpace(vector.dimensionality());
			}
			space.insertAll(vector);
		}
		
		return space == null ? new SimpleVectorSpace(0) : space;
	}

	public SimpleVectorSpace(int dimensionality) {
		super(dimensionality);
	}

	public SimpleVectorSpace(int dimensionality, Iterable<Vector> collection) {
		super(dimensionality);
		insertAll(collection);
	}

	public VectorSpace retrieveKnn(int k, final Vector prototype) {
		// do something like quick select instead...
		//System.out.println("SimpleVectorSpace.retrieveKnn" + this);
		List<Vector> q = new ArrayList<Vector>(values());
		//long before = System.nanoTime();
		Collections.sort(q, new Comparator<Vector>() {

			public int compare(Vector o1, Vector o2) {
				double d1 = o1.distance(prototype);
				// System.out.println("d(" + o1 + ", " + prototype + ")=" + d1);
				double d2 = o2.distance(prototype);
				// System.out.println("d(" + o2 + ", " + prototype + ")=" + d2);
				return d1 < d2 ? -1 : d1 == d2 ? 0 : +1;
			}
		});
		//System.out.println("sorted " + q.size() + " [" + (System.nanoTime() - before) + " ns]");
		List<Vector> subList = q.size() >= k ? q.subList(0, k) : q;
		return new SimpleVectorSpace(this.dimensionality(), subList);
	}

}
