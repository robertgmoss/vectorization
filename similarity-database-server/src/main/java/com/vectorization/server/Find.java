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
package com.vectorization.server;

import com.vectorization.core.SSVector;
import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.database.Database;

public class Find extends AbstractCommand  {

	private int k;
	private SSVector v;
	private String tableName;

	public Find(int k, SSVector v, String tableName) {
		this.k = k;
		this.v = v;
		this.tableName = tableName;
	}

	public String execute(Database database) {
		super.execute(database);
		
		SSCollection<SSVector> result = database.retrieveKnn(tableName, k, v);
		return result.toString();
	}
}
