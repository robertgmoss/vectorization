package com.vectorization.server;

import com.vectorization.core.Database;

public class Drop extends AbstractCommand  {

	private String tableName;

	public Drop(String input) {
		tableName = input;
	}

	public String execute(Database database) {
		super.execute(database);
		database.drop(tableName);
		return "table \"" + tableName + "\" dropped";
	}

}
