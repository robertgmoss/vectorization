package com.vectorization.server;

import com.vectorization.core.Database;
import com.vectorization.core.SSVector;
import com.vectorization.core.collection.SSCollection;

public class Find extends AbstractCommand  {

	private int k;
	private SSVector v;
	private String tableName;

	public Find(int k, SSVector v, String tableName) {
		this.k = k;
		this.v = v;
		this.tableName = tableName;
	}

	public String execute(Database database) {
		super.execute(database);
		
		SSCollection<SSVector> result = database.retrieveKnn(k, v, tableName);
		return result.toString();
	}
}
