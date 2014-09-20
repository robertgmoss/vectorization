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

import java.util.Collection;

import com.vectorization.core.SSVector;

public class VantagePointForest<E extends SSVector> extends CompositeCollection<E>{

	private static final long serialVersionUID = -1947039174911459830L;

	public VantagePointForest(int dimensionality) {
		super(dimensionality);
	}

	public VantagePointForest(int dimensionality,
			Collection<SSCollection<E>> value) {
		super(dimensionality, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SSCollection<E> newCollection() {
		return new VantagePointTree<E>(dimensionality());
	}

}
