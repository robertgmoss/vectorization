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

import com.vectorization.core.SSException;
import com.vectorization.core.SSVector;
import com.vectorization.core.Vectors;
import com.vectorization.core.database.Database;

public class Insert extends AbstractCommand  {

	private SSVector v;
	private String tableName;

	public Insert(String input) {
		String command = input.substring("insert".length()).trim();
		String vector = command.substring(command.indexOf("["), command.indexOf("]") + 1);
		v = Vectors.parseVector(vector);
		String normalize = command.substring(command.indexOf("]") + 1).trim();
		if (normalize.equals("normalized")) {
			v = Vectors.createNormalisedVector(v);
		}
	}

	public Insert(SSVector v, String tableName) {
		this.v = v;
		this.tableName = tableName;
	}

	public String execute(Database database) {
		super.execute(database);
		try {
			database.insertAndSave(tableName,v);
		} catch (SSException e) {
			return e.getMessage();
		}
		return "inserted " + v;
	}
}
