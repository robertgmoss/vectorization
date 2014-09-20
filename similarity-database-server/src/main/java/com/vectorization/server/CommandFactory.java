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

public interface CommandFactory {
	
	Database newDatabase(String name);

	Command newCreateCommand(String spaceName, int dimensionality);
	
	Command newDropCommand(String spaceName);
	
	Command newFindCommand(String spaceName, int k, SSVector v);
	
	Command newFindVectorCommand(String space, int k, String querySpace, String queryVectorName);
	
	Command newInsertCommand(String spaceName, SSVector v);
	
	Command newListCommand();
	
	Command newNullCommand(String msg);
	
	Command newRemoveCommand(String spaceName, String vectorName);
	
	Command newShowCommand(String spaceName, String vectorName);
	
	Command newUseCommand(String databaseName);

	Command newLoginCommand(String username, String password);
}
