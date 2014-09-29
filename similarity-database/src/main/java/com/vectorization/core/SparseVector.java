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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A sparse vector implementation that saves space by only storing non-zero dimensions
 * 
 * @author Robert Moss
 *
 */
public abstract class SparseVector extends AbstractVector {

	private static final long serialVersionUID = -6348541795286382173L;
	private Map<Integer, Double> v = new LinkedHashMap<Integer, Double>();
	private int dimensionality;

	public SparseVector(String id, Double... data) {
		this(id, Arrays.asList(data));
	}

	public SparseVector(String id, List<Double> data) {
		super(id);
		dimensionality = data.size();
		for (int i = 0; i < dimensionality; i++) {
			if (data.get(i) != 0.0) {
				v.put(i, data.get(i));
			}
		}
	}

	public int dimensionality() {
		return dimensionality;
	}

	public double get(int i) {
		if (!v.containsKey(i)) return 0;
		return v.get(i);
	}

}
