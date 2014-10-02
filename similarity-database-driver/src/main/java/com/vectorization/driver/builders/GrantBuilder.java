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
package com.vectorization.driver.builders;

public class GrantBuilder extends StatementBuilder {

	private String commands;

	public GrantBuilder(String statement, String... commands) {
		super(statement);
		StringBuilder commandList = new StringBuilder();
		for (int i = 0; i < commands.length; i++) {
			commandList.append(commands[i]);
			if (i < commands.length - 1)
				commandList.append(", ");
		}
		this.commands = commandList.toString();
	}

	public GrantBuilderIntermediate on(String database) {
		return new GrantBuilderIntermediate(getStatement() + commands + " on "
				+ database);
	}

	public GrantBuilderIntermediate on(String database, String space) {
		return new GrantBuilderIntermediate(getStatement() + commands + " on "
				+ database + "." + space);
	}

	public Builder to(String user) {
		return new GrantBuilderIntermediate(getStatement() + commands).to(user);
	}

}

class GrantBuilderIntermediate extends StatementBuilder {

	public GrantBuilderIntermediate(String statement) {
		super(statement);
	}

	public Builder to(String user) {
		return new Builder(getStatement() + " to " + user);
	}

}
