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

import com.vectorization.core.database.Database;

public class FindVector extends AbstractCommand{
	
	private String querySpaceName;
	private String queryVectorName;
	private String spaceName;
	private int k;

	public FindVector(int k, String querySpaceName, String queryVectorName, String spaceName) {
		this.k = k;
		this.querySpaceName = querySpaceName;
		this.queryVectorName = queryVectorName;
		this.spaceName = spaceName;
	}
	
	@Override
	public String execute(Database database) {
		return new Find(k, database.show(querySpaceName, queryVectorName),
				spaceName).execute(database);
	}
	
	public String getPermissionLevel() {
		StringBuilder sb = new StringBuilder();
		sb.append("find");
		sb.append(":");
		sb.append(querySpaceName);
		sb.append(",");
		sb.append(spaceName);
		return sb.toString();
	}

}
