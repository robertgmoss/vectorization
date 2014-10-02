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
package com.vectorization.core;


/**
 * A runtime exception for handling errors that occur without breaking abstraction.
 * 
 * @author Robert Moss
 *
 */
public class VectorizationException extends RuntimeException {

	private static final long serialVersionUID = -8250809360137371669L;

	public VectorizationException(String msg) {
		super(msg);
	}

	public VectorizationException(Exception e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		if(getCause() == null) return super.getMessage();
		return getCause().getMessage();
	}

}
