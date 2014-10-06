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

public class SecureCommandFactory implements CommandFactory{
	
	private CommandFactory delegate;
	
	public SecureCommandFactory(CommandFactory delegate) {
		this.delegate = delegate;
	}

	public Database newDatabase(String name) {
		return delegate.newDatabase(name);
	}

	public ServerCommand newCreateCommand(String spaceName, int dimensionality) {
		return new SecureCommand(delegate.newCreateCommand(spaceName, dimensionality));
	}

	public ServerCommand newDropCommand(String spaceName) {
		return new SecureCommand(delegate.newDropCommand(spaceName));
	}

	public ServerCommand newFindCommand(String spaceName, int k, Vector v) {
		return new SecureCommand(delegate.newFindCommand(spaceName, k, v));
	}

	public ServerCommand newFindVectorCommand(String space, int k, String querySpace,
			String queryVectorName) {
		return new SecureCommand(delegate.newFindVectorCommand(space, k, querySpace, queryVectorName));
	}

	public ServerCommand newInsertCommand(String spaceName, Vector v) {
		return new SecureCommand(delegate.newInsertCommand(spaceName, v));
	}

	public ServerCommand newListCommand() {
		return new SecureCommand(delegate.newListCommand());
	}

	public ServerCommand newNullCommand(String msg) {
		return new SecureCommand(new Null(msg));
	}

	public ServerCommand newRemoveCommand(String spaceName, String vectorName) {
		return new SecureCommand(delegate.newRemoveCommand(spaceName, vectorName));
	}

	public ServerCommand newShowCommand(String spaceName, String vectorName) {
		return new SecureCommand(delegate.newShowCommand(spaceName, vectorName));
	}

	public ServerCommand newUseCommand(String databaseName) {
		return new SecureCommand(delegate.newUseCommand(databaseName));
	}

	public ServerCommand newLoginCommand(String username, String password) {
		return delegate.newLoginCommand(username, password);
	}

	public ServerCommand newAddUserCommand(Security security,String username, String password) {
		return new SecureCommand(delegate.newAddUserCommand(security,username, password));
	}

	public ServerCommand newGrantCommand(Security security,List<String> permissions, String dbName,
			String spaceName, String username) {
		return new SecureCommand(delegate.newGrantCommand(security,permissions, dbName, spaceName, username));
	}

	public ServerCommand newChangePasswordCommand(Security security,String password) {
		return new SecureCommand(delegate.newChangePasswordCommand(security, password));
	}

}
