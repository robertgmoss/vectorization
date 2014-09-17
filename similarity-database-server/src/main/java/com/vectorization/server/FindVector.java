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

import com.vectorization.core.Database;

public class FindVector extends AbstractCommand{
	
	private String queryTableName;
	private String queryVectorName;
	private String tableName;
	private int k;

	public FindVector(int k, String queryTableName, String queryVectorName, String tableName) {
		this.k = k;
		this.queryTableName = queryTableName;
		this.queryVectorName = queryVectorName;
		this.tableName = tableName;
	}
	
	@Override
	public String execute(Database database) {
		return new Find(k, database.show(queryTableName, queryVectorName),
				tableName).execute(database);
	}

}
