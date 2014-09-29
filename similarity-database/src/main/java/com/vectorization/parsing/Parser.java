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

import com.vectorization.core.VectorizationException;
import com.vectorization.parsing.Token.Type;

public abstract class Parser<E> {

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
		if (!getLookAhead().type.equals(tokenType)) { throw new VectorizationException(
				"Syntax error: expected " + tokenType + "; found "
						+ getLookAhead().type); }
		consume();
	}

	public void match(Type tokenType, String keyword) {
		if (!getLookAhead().val.equals(keyword)) { throw new VectorizationException(
				"Syntax error: expected " + keyword + "; found "
						+ getLookAhead().val); }
		match(tokenType);
	}

	public Token getLookAhead() {
		return lookAhead;
	}

	public abstract E parse();
}
