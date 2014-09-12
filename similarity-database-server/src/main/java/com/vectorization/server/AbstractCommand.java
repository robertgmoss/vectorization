package com.vectorization.server;

import com.vectorization.core.Database;
import com.vectorization.core.SSException;

public class AbstractCommand implements Command{

	public String execute(Database database) {
		if(database == null) throw new SSException("Database is null");
		return "";
	}

}
