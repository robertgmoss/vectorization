package com.vectorization.driver.builders;

public class AddUserBuilder extends StatementBuilder{

	public AddUserBuilder(String statement) {
		super(statement);
	}
	
	public Builder identifiedBy(String password){
		return new Builder(getStatement() + "identified by " + password);
	}

}
