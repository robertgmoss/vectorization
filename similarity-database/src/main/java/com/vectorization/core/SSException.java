package com.vectorization.core;

import java.io.IOException;

public class SSException extends RuntimeException {

	private static final long serialVersionUID = -8250809360137371669L;

	public SSException(String msg) {
		super(msg);
	}

	public SSException(IOException e) {
		super(e);
	}

}
