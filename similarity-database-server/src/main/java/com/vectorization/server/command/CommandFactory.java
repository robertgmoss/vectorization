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

import java.util.List;

import com.vectorization.core.Vector;
import com.vectorization.core.database.Database;
import com.vectorization.parsing.ServerCommand;
import com.vectorization.server.security.Security;

public interface CommandFactory {
	
	Database newDatabase(String name);

	ServerCommand newCreateCommand(String spaceName, int dimensionality);
	
	ServerCommand newDropCommand(String spaceName);
	
	ServerCommand newFindCommand(String spaceName, int k, Vector v);
	
	ServerCommand newFindVectorCommand(String space, int k, String querySpace, String queryVectorName);
	
	ServerCommand newInsertCommand(String spaceName, Vector v);
	
	ServerCommand newListCommand();
	
	ServerCommand newNullCommand(String msg);
	
	ServerCommand newRemoveCommand(String spaceName, String vectorName);
	
	ServerCommand newShowCommand(String spaceName, String vectorName);
	
	ServerCommand newUseCommand(String databaseName);

	ServerCommand newLoginCommand(String username, String password);

	ServerCommand newAddUserCommand(Security security,String username, String password);

	ServerCommand newGrantCommand(Security security,List<String> permissions, String dbName,
			String spaceName, String username);

	ServerCommand newChangePasswordCommand(Security security, String password);
}
