package com.vectorization.server;

import java.util.Iterator;

import com.vectorization.core.Database;

public class List extends AbstractCommand {

	public String execute(Database database) {
		super.execute(database);
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = database.list().iterator();
		sb.append("[");
		while (it.hasNext()) {
			sb.append("\"" + it.next() + "\"");
			if (it.hasNext()) sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

}
