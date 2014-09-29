package com.vectorization.driver.builders;

public class GrantBuilder extends StatementBuilder {

	private String commands;

	public GrantBuilder(String statement, String... commands) {
		super(statement);
		StringBuilder commandList = new StringBuilder();
		for (int i = 0; i < commands.length; i++) {
			commandList.append(commands[i]);
			if (i < commands.length - 1)
				commandList.append(", ");
		}
		this.commands = commandList.toString();
	}

	public GrantBuilderIntermediate on(String database) {
		return new GrantBuilderIntermediate(getStatement() + commands + " on "
				+ database);
	}

	public GrantBuilderIntermediate on(String database, String space) {
		return new GrantBuilderIntermediate(getStatement() + commands + " on "
				+ database + "." + space);
	}

	public Builder to(String user) {
		return new GrantBuilderIntermediate(getStatement() + commands).to(user);
	}

}

class GrantBuilderIntermediate extends StatementBuilder {

	public GrantBuilderIntermediate(String statement) {
		super(statement);
	}

	public Builder to(String user) {
		return new Builder(getStatement() + " to " + user);
	}

}
