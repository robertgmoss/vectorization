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

/**
 * A decorator for vectors that provides a view whereby the vector is normalised
 * such that the sum of all dimensions equals one.
 * 
 * @author Robert Moss
 *
 */
public class NormalisedVector extends SSVectorDecorator {

	private static final long serialVersionUID = -7461934269703184566L;
	private double sum = 0.0;

	public NormalisedVector(SSVector vector) {
		super(vector);
		for (int i = 0; i < dimensionality(); i++) {
			sum += super.get(i);
		}
	}

	@Override
	public double get(int i) {
		return super.get(i) / sum;
	}

	public double distance(SSVector other) {
		return super.distance(other);
	}

}
