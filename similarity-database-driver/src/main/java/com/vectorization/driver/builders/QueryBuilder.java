package com.vectorization.driver.builders;

import com.vectorization.core.collection.VectorSpace;
import com.vectorization.driver.Connection;

public class QueryBuilder extends StatementBuilder{

	public QueryBuilder(String statement) {
		super(statement);
	}
	
	public VectorSpace execute(Connection conn) {
		return conn.createStatement().executeQuery(getStatement());
	}

}
