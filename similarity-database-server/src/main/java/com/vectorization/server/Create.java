package com.vectorization.server;

import com.vectorization.core.Database;

public class Create extends AbstractCommand {

	private String tableName;
	private int dimensionality;

	public Create(String tableName, int dimensionality) {
		this.tableName = tableName;
		this.dimensionality = dimensionality;

	}

	public String execute(Database database) {
		super.execute(database);
		database.create(tableName, dimensionality);
		return "table \"" + tableName + "\" created";
	}
}
