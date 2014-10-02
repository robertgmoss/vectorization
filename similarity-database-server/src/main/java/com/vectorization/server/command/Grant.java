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

import java.sql.SQLException;
import java.util.List;

import com.vectorization.core.VectorizationException;
import com.vectorization.core.database.Database;
import com.vectorization.server.security.Security;

public class Grant extends AbstractCommand {

	private List<String> permissions;
	private String dbName;
	private String spaceName;
	private String username;
	private Security security;

	public Grant(Security security, List<String> permissions, String dbName, String spaceName,
			String username) {
		this.security = security;
		this.permissions = permissions;
		this.dbName = dbName;
		this.spaceName = spaceName;
		this.username = username;
	}

	@Override
	public String execute(Database database) {
		String roleName = username; // pick a role name for these permissions
		try {
			StringBuilder permissionBuilder = new StringBuilder();
			for(int i = 0; i < permissions.size(); i++){
				 permissionBuilder.append(permissions.get(i));
				 if(i < permissions.size() - 1) permissionBuilder.append(",");
				
			}
			String commands = permissionBuilder.toString();
			
			String permission = dbName + ":" + commands + ":" + spaceName;
			security.insertRolePermission(roleName, permission);
			//security.insertUserRole(username, roleName);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
		return "permissions granted ";
	}

	public String getPermissionLevel() {
		return "grant";
	}

}
