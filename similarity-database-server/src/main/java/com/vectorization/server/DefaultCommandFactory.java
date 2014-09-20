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
import com.vectorization.core.database.DatabaseImpl;
import com.vectorization.parsing.Command;

public class DefaultCommandFactory implements CommandFactory{

	public Database newDatabase(String name) {
		return new DatabaseImpl(name);
	}

	public Command newCreateCommand(String spaceName, int dimensionality) {
		return new Create(spaceName, dimensionality);
	}

	public Command newDropCommand(String spaceName) {
		return new Drop(spaceName);
	}

	public Command newFindCommand(String spaceName, int k, SSVector v) {
		return new Find(k, v, spaceName);
	}

	public Command newFindVectorCommand(String space, int k, String querySpace,
			String queryVectorName) {
		return new FindVector(k, querySpace, queryVectorName, space);
	}

	public Command newInsertCommand(String spaceName, SSVector v) {
		return new Insert(v, spaceName);
	}

	public Command newListCommand() {
		return new List();
	}

	public Command newNullCommand(String msg) {
		return new Null(msg);
	}

	public Command newRemoveCommand(String spaceName, String vectorName) {
		return new Remove(spaceName, vectorName);
	}

	public Command newShowCommand(String spaceName, String vectorName) {
		return new Show(spaceName, vectorName);
	}

	public Command newUseCommand(String databaseName) {
		return new Use(databaseName);
	}

	public Command newLoginCommand(String username, String password) {
		return new Login(username, password);
	}

}
