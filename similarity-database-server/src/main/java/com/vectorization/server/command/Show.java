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
package com.vectorization.server.command;

import com.vectorization.core.VectorizationException;
import com.vectorization.core.database.Database;

public class Show extends AbstractCommand  {

	private String tableName;
	private String vectorName;

	public Show(String tableName, String vectorName) {
		this.tableName = tableName;
		this.vectorName = vectorName;
	}

	public String execute(Database database) {
		super.execute(database);
		String result = "";
		if (tableName.equals("")) throw new VectorizationException("No space selected");
		else if(vectorName.equals("")) result = database.show(tableName).toString();
		else result = database.show(tableName, vectorName).toString();
		return result;
	}
	
	public String getPermissionLevel() {
		StringBuilder sb = new StringBuilder();
		sb.append("show");
		sb.append(":");
		sb.append(tableName);
		sb.append(":");
		sb.append(vectorName);
		return sb.toString();
	}

}
