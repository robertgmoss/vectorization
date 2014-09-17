package com.vectorization.server;

import com.vectorization.core.Database;
import com.vectorization.core.SSException;

public class Remove extends AbstractCommand  {
	
	private String tableName;
	private String v;


	public Remove(String tableName, String v) {
		this.tableName = tableName;
		this.v = v;
	}

	
	public String execute(Database database) {
		super.execute(database);
		try {
			database.removeAndSave(tableName,v);
		} catch (SSException e) {
			return e.getMessage();
		}
		return "removed  " + v;
	}
}
