package com.vectorization.driver;


public class DummyHandler implements Handler {

	public String processRequest(String command) {
		return "dummy handler";
	}

}
