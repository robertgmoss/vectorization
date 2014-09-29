package com.vectorization.driver.builders;

public abstract class StatementBuilder {

	private String statement;
	
	public StatementBuilder(String statement) {
		this.statement = statement;
	}
	
	public String getStatement() {
		return statement;
	}
}
