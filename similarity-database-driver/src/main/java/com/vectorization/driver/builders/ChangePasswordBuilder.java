package com.vectorization.driver.builders;

public class ChangePasswordBuilder extends StatementBuilder{

	public ChangePasswordBuilder(String statement) {
		super(statement);
	}
	
	public Builder to(String password){
		return new Builder(getStatement() + " to " + password);
	}

}
