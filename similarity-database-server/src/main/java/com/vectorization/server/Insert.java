package com.vectorization.server;

import com.vectorization.core.Database;
import com.vectorization.core.SSException;
import com.vectorization.core.SSVector;
import com.vectorization.core.Vectors;

public class Insert extends AbstractCommand  {

	private SSVector v;
	private String tableName;

	public Insert(String input) {
		String command = input.substring("insert".length()).trim();
		String vector = command.substring(command.indexOf("["), command.indexOf("]") + 1);
		v = Vectors.parseVector(vector);
		String normalize = command.substring(command.indexOf("]") + 1).trim();
		if (normalize.equals("normalized")) {
			v = Vectors.createNormalisedVector(v);
		}
	}

	public Insert(SSVector v) {
		this.v = v;
	}

	public Insert(SSVector v, String tableName) {
		this(v);
		this.tableName = tableName;
	}

	public String execute(Database database) {
		super.execute(database);
		try {
			database.insert(v, tableName);
		} catch (SSException e) {
			return e.getMessage();
		}
		return "inserted " + v;
	}
}
