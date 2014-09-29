package com.vectorization.server;

import java.io.IOException;

import com.vectorization.core.database.Database;

public interface Processor extends Runnable{

	void setDatabase(Database database);

	void close() throws IOException;
}
