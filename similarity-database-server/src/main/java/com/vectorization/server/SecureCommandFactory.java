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
import com.vectorization.core.database.Database;
import com.vectorization.parsing.Command;

public class SecureCommandFactory implements CommandFactory{
	
	private CommandFactory delegate;
	
	public SecureCommandFactory(CommandFactory delegate) {
		this.delegate = delegate;
	}

	public Database newDatabase(String name) {
		return delegate.newDatabase(name);
	}

	public Command newCreateCommand(String spaceName, int dimensionality) {
		return new SecureCommand(delegate.newCreateCommand(spaceName, dimensionality));
	}

	public Command newDropCommand(String spaceName) {
		return new SecureCommand(delegate.newDropCommand(spaceName));
	}

	public Command newFindCommand(String spaceName, int k, SSVector v) {
		return new SecureCommand(delegate.newFindCommand(spaceName, k, v));
	}

	public Command newFindVectorCommand(String space, int k, String querySpace,
			String queryVectorName) {
		return new SecureCommand(delegate.newFindVectorCommand(space, k, querySpace, queryVectorName));
	}

	public Command newInsertCommand(String spaceName, SSVector v) {
		return new SecureCommand(delegate.newInsertCommand(spaceName, v));
	}

	public Command newListCommand() {
		return new SecureCommand(delegate.newListCommand());
	}

	public Command newNullCommand(String msg) {
		return new SecureCommand(new Null(msg));
	}

	public Command newRemoveCommand(String spaceName, String vectorName) {
		return new SecureCommand(delegate.newRemoveCommand(spaceName, vectorName));
	}

	public Command newShowCommand(String spaceName, String vectorName) {
		return new SecureCommand(delegate.newShowCommand(spaceName, vectorName));
	}

	public Command newUseCommand(String databaseName) {
		return new SecureCommand(delegate.newUseCommand(databaseName));
	}

	public Command newLoginCommand(String username, String password) {
		return delegate.newLoginCommand(username, password);
	}

}
