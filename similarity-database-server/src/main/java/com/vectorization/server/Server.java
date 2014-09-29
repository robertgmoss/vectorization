package com.vectorization.server;

public interface Server {

	void run();
	
	void shutDown();

	void setPort(int port);
}
