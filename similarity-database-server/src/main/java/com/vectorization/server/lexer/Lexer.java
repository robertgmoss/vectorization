package com.vectorization.server.lexer;

import com.vectorization.core.SSException;

public abstract class Lexer {

	public static final char EOF = (char) -1;

	private String input;
	private int position = 0;
	private char lookAhead;

	public Lexer(String input) {
		this.input = input;
		if(input.equals("")) this.lookAhead = EOF;
		else this.lookAhead = input.charAt(position);
	}

	public void consume() {
		position++;
		if (position >= input.length()) lookAhead = EOF;
		else lookAhead = input.charAt(position);
	}

	public void match(char c) {
		if (c == getLookAhead()) consume();
		else throw new SSException("Expected " + c + " found " + getLookAhead());
	}

	public char getLookAhead() {
		return lookAhead;
	}

	public abstract Token next();

	public boolean hasNext() {
		return lookAhead != EOF;
	}

}
