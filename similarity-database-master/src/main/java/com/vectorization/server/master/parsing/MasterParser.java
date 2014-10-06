package com.vectorization.server.master.parsing;

import com.vectorization.driver.Vectorization;
import com.vectorization.parsing.Lexer;
import com.vectorization.parsing.Parser;
import com.vectorization.parsing.ServerLexer.SSType;
import com.vectorization.server.master.command.AddServerCommand;
import com.vectorization.server.master.command.MasterCommand;

public class MasterParser extends Parser<MasterCommand> {

	private MasterCommand defaultCommand;

	public MasterParser(Lexer l, MasterCommand defaultCommand) {
		super(l);
		this.defaultCommand = defaultCommand;
	}

	@Override
	public MasterCommand parse() {
		if (getLookAhead().type.equals(SSType.NAME)) {
			if (getLookAhead().val.equals("connect")) {
				return addServer();
			}
		}
		return defaultCommand;
	}

	private MasterCommand addServer() {
		match(SSType.NAME, "connect");
		match(SSType.NAME, "server");
		String name = name();
		match(SSType.NAME, "at");
		String address = name();
		int port = Vectorization.DEFAULT_PORT;
		if (getLookAhead().type.equals(SSType.NAME)) {
			match(SSType.NAME, "on");
			match(SSType.NAME, "port");
			port = integer();
		}
		return new AddServerCommand(name, address, port);
	}

	private String name() {
		String name = getLookAhead().val;
		match(SSType.NAME);
		return name;
	}

	private int integer() {
		int integer = Integer.parseInt(getLookAhead().val);
		match(SSType.NUMBER);
		return integer;
	}

}
