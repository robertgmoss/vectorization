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

	public Parser<Command> create(Processor processor, String input) {
		Lexer l = lexerFactory.create(input);
		return new ServerParser(security, commandFactory, processor, l);
	}

}
