package com.vectorization.driver.builders;

public class LoginBuilder extends StatementBuilder{

	public LoginBuilder(String statement) {
		super(statement);
	}
	
	public Builder with(String password){
		return new Builder(getStatement() + " with " + password);
	}

}
