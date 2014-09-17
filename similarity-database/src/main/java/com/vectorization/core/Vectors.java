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
package com.vectorization.core;

import java.util.List;

/**
 * A convenience class for creating vectors in the system.
 * 
 * @author Robert Moss
 *
 */
public class Vectors {
	
	public static final double MAX_DISTANCE = 1.0;

	public static SSVector createNormalisedVector(String id, Double... ds) {
		return new NormalisedVector(createVector(id, ds));
	}

	public static SSVector createNormalisedVector(SSVector v) {
		return new NormalisedVector(v);
	}

	public static SSVector createVector(String id, Double... ds) {
		return new SparseVector(id, ds);
	}

	public static SSVector createVector(String id, List<Double> ds) {
		return new SparseVector(id, ds);
	}

	public static SSVector parseVector(String v) {
		String s = v.substring(v.indexOf("[") + 1, v.indexOf("]"));
		if (s.equals("")) return createVector("");
		String[] dims = s.split(",");
		Double[] ds = new Double[dims.length];
		for (int i = 0; i < dims.length; i++) {
			ds[i] = Double.parseDouble(dims[i].trim());
		}
		return createVector("", ds);
	}

	public static void main(String[] args) {
		SSVector vector = createVector("1", 1.1, 2.1, 0.0);
		System.out.println(vector);
		SSVector parseVector = parseVector(vector.toString());
		System.out.println(parseVector);
		System.out.println(createNormalisedVector(parseVector));

		vector = createVector("-1");
		System.out.println(vector);
		parseVector = parseVector(vector.toString());
		System.out.println(parseVector);
		System.out.println(createNormalisedVector(parseVector));

	}
}
