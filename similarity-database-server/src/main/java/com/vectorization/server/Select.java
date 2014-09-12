package com.vectorization.server;

import com.vectorization.core.Database;

public class Select extends AbstractCommand {

	private String tableName;

	public Select(String tableName) {
		this.tableName = tableName;
	}

	public String execute(Database database) {
		super.execute(database);
		database.select(tableName);
		return tableName + " selected";
	}
}
