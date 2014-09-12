package com.vectorization.server;

import com.vectorization.core.Database;

public interface Command {

	String execute(Database database);

}
