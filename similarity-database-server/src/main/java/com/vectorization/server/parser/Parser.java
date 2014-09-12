package com.vectorization.server.parser;

import com.vectorization.server.Command;
import com.vectorization.server.lexer.Lexer;
import com.vectorization.server.lexer.Token;
import com.vectorization.server.lexer.Token.Type;

import com.vectorization.core.SSException;

public abstract class Parser {

	private final Lexer l;
	private Token lookAhead;

	public Parser(Lexer l) {
		this.l = l;
		lookAhead = l.next();
	}

	public void consume() {
		lookAhead = l.next();
	}

	public void match(Type tokenType) {
		if (!getLookAhead().type.equals(tokenType)) { throw new SSException(
				"Syntax error: expected " + tokenType + "; found "
						+ getLookAhead().type); }
		consume();
	}

	public void match(Type tokenType, String keyword) {
		if (!getLookAhead().val.equals(keyword)) { throw new SSException(
				"Syntax error: expected " + keyword + "; found "
						+ getLookAhead().val); }
		match(tokenType);
	}

	public Token getLookAhead() {
		return lookAhead;
	}

	public abstract Command parse();
}
