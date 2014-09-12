package com.vectorization.server;

import com.vectorization.core.Database;

public class FindVector extends AbstractCommand{
	
	private String queryTableName;
	private String queryVectorName;
	private String tableName;
	private int k;

	public FindVector(int k, String queryTableName, String queryVectorName, String tableName) {
		this.k = k;
		this.queryTableName = queryTableName;
		this.queryVectorName = queryVectorName;
		this.tableName = tableName;
	}
	
	@Override
	public String execute(Database database) {
		return new Find(k, database.show(queryTableName, queryVectorName),
				tableName).execute(database);
	}

}
