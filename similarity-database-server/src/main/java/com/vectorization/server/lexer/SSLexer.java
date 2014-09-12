package com.vectorization.server.lexer;

import com.vectorization.server.lexer.Token.Type;

public class SSLexer extends Lexer {

	public enum SSType implements Type {
		NUMBER, NAME, COMMA, DOT, EQUALS, LBRACK, RBRACK, EOF_TYPE
	}

	public SSLexer(String input) {
		super(input);
	}

	private boolean isPoint() {
		return getLookAhead() == '.';
	}

	private boolean isComma() {
		return getLookAhead() == ',';
	}

	private boolean isLBrack() {
		return getLookAhead() == '[';
	}

	private boolean isRBrack() {
		return getLookAhead() == ']';
	}

	private boolean isEquals() {
		return getLookAhead() == '=';
	}

	private boolean isDigit() {
		return Character.isDigit(getLookAhead());
	}

	private void consumeWhiteSpace() {
		while (Character.isWhitespace(getLookAhead())) {
			consume();
		}
	}

	private Token consumeName() {
		StringBuilder sb = new StringBuilder();
		if (isIdentifierStart()) {
			sb.append(getLookAhead());
			consume();
		}

		while (Character.isJavaIdentifierPart(getLookAhead())) {
			sb.append(getLookAhead());
			consume();
		}
		return new Token(SSType.NAME, sb.toString());
	}

	private boolean isIdentifierStart() {
		return Character.isJavaIdentifierStart(getLookAhead());
	}

	private Token consumeNumber() {
		StringBuilder sb = new StringBuilder();
		while (isDigit()) {
			sb.append(getLookAhead());
			consume();
		}
		if (isPoint()) {
			sb.append(getLookAhead());
			consume();
		}
		while (isDigit()) {
			sb.append(getLookAhead());
			consume();
		}
		return new Token(SSType.NUMBER, sb.toString());
	}

	@Override
	public Token next() {
		consumeWhiteSpace();
		if (isLBrack()) {
			consume();
			return new Token(SSType.LBRACK, "[");
		}
		if (isRBrack()) {
			consume();
			return new Token(SSType.RBRACK, "]");
		}
		if (isComma()) {
			consume();
			return new Token(SSType.COMMA, ",");
		}
		if (isPoint()) {
			consume();
			return new Token(SSType.DOT, ".");
		}
		if (isEquals()) {
			consume();
			return new Token(SSType.EQUALS, "=");
		}
		if (isDigit()) return consumeNumber();
		if (isIdentifierStart()) return consumeName();
		return new Token(SSType.EOF_TYPE, "");
	}

	public static void main(String[] args) {
		Lexer test = new SSLexer("create test dimensionality 5");
		while (test.hasNext()) {
			System.out.println(test.next());
		}
		System.out.println();
		test = new SSLexer("insert vectorid.a = [5.1, 2.1] into n812um");
		while (test.hasNext()) {
			System.out.println(test.next());
		}

	}

}
