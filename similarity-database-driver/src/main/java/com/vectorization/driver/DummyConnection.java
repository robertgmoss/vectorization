package com.vectorization.driver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class DummyConnection extends VectorizationConnection {

	public DummyConnection(String address, int port) {
		super(address, port);
	}
	
	@Override
	public void connect() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void connect(String address, int port) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected Handler createHandler(BufferedReader in, PrintWriter out) {
		return new DummyHandler();
	}

}
