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

import java.io.File;
import java.util.Arrays;

import com.vectorization.core.collection.FileCompositeCollection;
import com.vectorization.core.collection.VPFCollection;

public class VantagePointForestSpaceFactory implements SpaceFactory{

	public FileCompositeCollection createSpace(int dimensionality,String database, String name) {
		FileCompositeCollection collection = new VPFCollection(dimensionality,database, name);
		return collection;
	}

	public FileCompositeCollection createSpace(int dimensionality, String database, String name, File... files) {
		FileCompositeCollection collection = new VPFCollection(dimensionality,database, name, Arrays.asList(files));
		return collection;
	}

}
