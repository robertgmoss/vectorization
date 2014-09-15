package com.vectorization.core;

import java.io.IOException;

/**
 * A runtime exception for handling errors that occur without breaking abstraction.
 * 
 * @author Robert Moss
 *
 */
public class SSException extends RuntimeException {

	private static final long serialVersionUID = -8250809360137371669L;

	public SSException(String msg) {
		super(msg);
	}

	public SSException(IOException e) {
		super(e);
	}

}
