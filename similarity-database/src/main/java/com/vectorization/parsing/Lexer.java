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
	
	protected void consumeWhiteSpace() {
		while (Character.isWhitespace(getLookAhead())) {
			consume();
		}
	}

}
