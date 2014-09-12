package com.vectorization.server.lexer;

public class Token {

	public interface Type {
	}

	public final Type type;
	public final String val;

	public Token(Type name, String string) {
		type = name;
		val = string;
	}

	@Override
	public String toString() {
		return val + ": " + type;
	}

}
