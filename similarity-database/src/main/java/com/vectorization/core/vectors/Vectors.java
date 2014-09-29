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
package com.vectorization.core.vectors;

import java.util.List;

import com.vectorization.core.NormalisedVector;
import com.vectorization.core.Vector;

/**
 * A convenience class for creating vectors in the system.
 * 
 * @author Robert Moss
 *
 */
public class Vectors {
	
	public static final double MAX_DISTANCE = 1.0;

	public static Vector createNormalisedVector(String id, Double... ds) {
		return new NormalisedVector(createVector(id, ds));
	}

	public static Vector createNormalisedVector(Vector v) {
		return new NormalisedVector(v);
	}

	public static Vector createVector(String id, Double... ds) {
		return new SEDVector(id, ds);
	}

	public static Vector createVector(String id, List<Double> ds) {
		return new SEDVector(id, ds);
	}

	public static Vector parseVector(String v) {
		return parseVector("", v);
	}
	
	public static Vector parseVector(String id, String v){
		String s = v.substring(v.indexOf("[") + 1, v.indexOf("]"));
		if (s.equals("")) return createVector(id);
		String[] dims = s.split(",");
		Double[] ds = new Double[dims.length];
		for (int i = 0; i < dims.length; i++) {
			ds[i] = Double.parseDouble(dims[i].trim());
		}
		return createVector(id, ds);
	}

	public static void main(String[] args) {
		Vector vector = createVector("1", 1.1, 2.1, 0.0);
		System.out.println(vector);
		Vector parseVector = parseVector(vector.toString());
		System.out.println(parseVector);
		System.out.println(createNormalisedVector(parseVector));

		vector = createVector("-1");
		System.out.println(vector);
		parseVector = parseVector(vector.toString());
		System.out.println(parseVector);
		System.out.println(createNormalisedVector(parseVector));

	}
}
