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

import com.vectorization.core.Vector;

public interface Database {

	Database create(String spaceName, int dimensionality);

	void drop(String space);

	Iterable<Vector> retrieveKnn(String spaceName, int k, Vector prototype);

	Database remove(String table, String... vectors);

	Database insert(String table, Vector... vectors);

	Vector show(String spaceName, String vectorName);

	Iterable<Vector> show(String spaceName);

	Iterable<String> list();

	String getName();

}