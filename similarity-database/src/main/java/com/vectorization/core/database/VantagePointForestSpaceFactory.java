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
package com.vectorization.core.database;

import java.util.Collection;

import com.vectorization.core.collection.VPFCollection;
import com.vectorization.core.collection.VectorCollection;

public class VantagePointForestSpaceFactory implements SpaceFactory{

	public VectorCollection createSpace(int dimensionality) {
		return new VPFCollection(dimensionality);
	}

	public VectorCollection createCompositeTable(int dimensionality,
			Collection<VectorCollection> list) {
		long before = System.nanoTime();
		VPFCollection collection = new VPFCollection(dimensionality, list);
		System.out.println("created vpf [" + (System.nanoTime() - before) + " ns]");
		return collection;
	}

}
