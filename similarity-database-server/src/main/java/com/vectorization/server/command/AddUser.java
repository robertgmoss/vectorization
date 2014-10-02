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

import com.vectorization.core.VectorizationException;
import com.vectorization.core.database.Database;
import com.vectorization.server.security.Security;

public class AddUser extends AbstractCommand{
	
	private String username;
	private String password;
	private Security security;

	public AddUser(Security security, String username, String password) {
		this.security = security;
		this.username = username;
		this.password = password;
	}

	@Override
	public String execute(Database database) {
		try {
			security.insertUser(username, password);
			security.insertRole(username);
			security.insertUserRole(username, username);
			security.insertUserRole(username, "user");
			return "user added";
		} catch (SQLException e) {
			throw new VectorizationException("can't add user: "+ e.getMessage());
		}
	}

	public String getPermissionLevel() {
		return "adduser";
	}

}
