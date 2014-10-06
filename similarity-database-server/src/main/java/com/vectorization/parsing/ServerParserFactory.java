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
package com.vectorization.parsing;

import com.google.inject.Inject;
import com.vectorization.server.Processor;
import com.vectorization.server.command.CommandFactory;
import com.vectorization.server.security.Security;

public class ServerParserFactory implements ParserFactory {
	
	private LexerFactory lexerFactory;
	private Security security;
	private CommandFactory commandFactory;

	@Inject
	public ServerParserFactory(Security security, CommandFactory commandFactory, LexerFactory lexerFactory) {
		this.security = security;
		this.commandFactory = commandFactory;
		this.lexerFactory = lexerFactory;
	}

	public Parser<ServerCommand> create(Processor processor, String input) {
		Lexer l = lexerFactory.create(input);
		return new ServerParser(security, commandFactory, processor, l);
	}

}
