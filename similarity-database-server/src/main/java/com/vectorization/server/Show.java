package com.vectorization.server;

import com.vectorization.core.Database;

public class Show extends AbstractCommand  {

	private String tableName;
	private String vectorName;

	public Show(String tableName, String vectorName) {
		this.tableName = tableName;
		this.vectorName = vectorName;
	}

	public String execute(Database database) {
		super.execute(database);
		String result = "";
		if (tableName.equals("")) result = database.show().toString();
		else if(vectorName.equals("")) result = database.show(tableName).toString();
		else result = database.show(tableName, vectorName).toString();
		return result;
	}

}
