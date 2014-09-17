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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vectorization.core.SSVector;

/**
 * A simple implementation that uses the default methods of it superclass
 * to organise the data.  As a result the implementation of the retrieveKnn
 * method is naive whereby a sort is run every query.
 * 
 * @author Robert Moss
 *
 * @param <E>
 */
public class Space<E extends SSVector> extends AbstractCollection<E> {

	private static final long serialVersionUID = 2751516171619962956L;

	public Space(int dimensionality) {
		super(dimensionality);
	}

	public Space(int dimensionality, Collection<E> collection) {
		super(dimensionality);
		for (E e : collection) {
			insert(e);
		}
	}

	public SSCollection<E> retrieveKnn(int k, final SSVector prototype) {
		// do something like quick select instead...

		List<E> q = new ArrayList<E>(values());
		Collections.sort(q, new Comparator<E>() {

			public int compare(E o1, E o2) {
				double d1 = o1.distance(prototype);
				// System.out.println("d(" + o1 + ", " + prototype + ")=" + d1);
				double d2 = o2.distance(prototype);
				// System.out.println("d(" + o2 + ", " + prototype + ")=" + d2);
				return d1 < d2 ? -1 : d1 == d2 ? 0 : +1;
			}
		});
		List<E> subList = q.size() >= k ? q.subList(0, k) : q;
		return new Space<E>(this.dimensionality(), subList);
	}

}
