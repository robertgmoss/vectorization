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

import java.util.Iterator;

import com.vectorization.core.database.Database;

public class List extends AbstractCommand {

	public String execute(Database database) {
		super.execute(database);
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = database.list().iterator();
		sb.append("[");
		while (it.hasNext()) {
			sb.append("\"" + it.next() + "\"");
			if (it.hasNext()) sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

}
