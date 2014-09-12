package com.vectorization.server.parser;

import com.vectorization.core.Database;
import com.vectorization.server.Command;

public class Use implements Command {
	
	private String dbName;

	public Use(String dbName) {
		this.dbName = dbName;
	}

	public String execute(Database database) {
		return "Using database: " + dbName;
	}

}
