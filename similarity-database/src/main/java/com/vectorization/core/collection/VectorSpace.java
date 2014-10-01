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

import com.vectorization.core.Vector;

/**
 * Provides the type for collections that are stored in the system.
 * 
 * @author Robert Moss
 *
 * @param <V>
 */
public interface VectorSpace extends MetricSpace<Vector>, Iterable<Vector> {
	
	VectorSpace removeAll(String... vectorName);
	
	VectorSpace removeAll(Iterable<String> myObjects);

	boolean contains(Vector o);
	
	boolean contains(String v);

	int size();

	int dimensionality();

	Vector get(String vectorName);

	void save(String filename) throws IOException;
	
	VectorSpace insertAll(Vector... myObjects);
	
	VectorSpace insertAll(MetricSpace<Vector> myObjects);
	
	Iterable<Vector> retrieveKnn(int k, Vector prototype);

	VectorSpace insertAll(Iterable<Vector> subList);

}
