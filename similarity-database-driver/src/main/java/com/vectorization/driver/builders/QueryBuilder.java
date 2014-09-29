package com.vectorization.driver.builders;

import com.vectorization.core.collection.VectorCollection;
import com.vectorization.driver.Connection;

public class QueryBuilder extends StatementBuilder{

	public QueryBuilder(String statement) {
		super(statement);
	}
	
	public VectorCollection execute(Connection conn) {
		return conn.createStatement().executeQuery(getStatement());
	}

}
