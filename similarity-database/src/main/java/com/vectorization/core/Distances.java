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
 * Provides distance calculations according to different metric definitions
 * 
 * @author Robert Moss
 *
 */
public class Distances {

	public static double structuralEntropicDistance(SSVector x, SSVector y) {
		if (x.dimensionality() != y.dimensionality()) throw new SSException(
				"dimensionality must be equal");
		double sum = 0.0;
		for (int i = 0; i < x.dimensionality(); i++) {
			double a = x.get(i);
			double b = y.get(i);
			if (a == 0 || b == 0) continue;
			sum += (a + b) * log2(a + b) - (a * log2(a) + b * log2(b));

		}
		return Math.pow(2, 1 - 0.5 * sum) - 1;
	}

	private static double log2(double x) {
		return x == 0.0 ? 0.0 : Math.log(x) / Math.log(2);
	}

}
