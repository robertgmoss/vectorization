package com.vectorization.server;

import com.vectorization.core.Database;

public class Null extends AbstractCommand {

	private String input;

	public Null(String input) {
		this.input = input;
	}

	public String execute(Database database) {
		super.execute(database);
		return "No such command: " + input;
	}

}
