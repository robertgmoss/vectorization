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
import com.google.inject.assistedinject.Assisted;
import com.vectorization.parsing.Token.Type;

public class ServerLexer extends Lexer {

	public enum SSType implements Type {
		NUMBER, NAME,STAR, COMMA, DOT, EQUALS, LBRACK, RBRACK, EOF_TYPE
	}

	@Inject
	public ServerLexer(@Assisted String input) {
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
	
	private boolean isStar(){
		return getLookAhead() == '*';
	}

	private boolean isDigit() {
		return Character.isDigit(getLookAhead());
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
		if (isStar()) {
			consume();
			return new Token(SSType.STAR, "*");
		}
		if (isDigit()) return consumeNumber();
		if (isIdentifierStart()) return consumeName();
		return new Token(SSType.EOF_TYPE, "");
	}
}
