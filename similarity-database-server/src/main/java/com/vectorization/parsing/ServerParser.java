/*  
 *  Copyright (C) 2014 Robert Moss
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.vectorization.parsing;

import java.util.ArrayList;
import java.util.List;

import com.vectorization.core.SSVector;
import com.vectorization.core.Vectors;
import com.vectorization.core.database.Database;
import com.vectorization.parsing.ServerLexer.SSType;
import com.vectorization.server.CommandFactory;
import com.vectorization.server.DefaultCommandFactory;
import com.vectorization.server.Processor;
import com.vectorization.server.SecureCommandFactory;

public class ServerParser extends Parser<Command> {

	private Processor processor;
	private CommandFactory factory = new SecureCommandFactory(new DefaultCommandFactory());

	public ServerParser(Processor processor, Lexer l) {
		super(l);
		this.processor = processor;
	}

	@Override
	public Command parse() {
		if (getLookAhead().type.equals(SSType.NAME)) {
			if (getLookAhead().val.equals("login")) { return login(); }
			if (getLookAhead().val.equals("use")) { return use(); }
			if (getLookAhead().val.equals("create")) { return create(); }
			if (getLookAhead().val.equals("drop")) { return drop(); }
			if (getLookAhead().val.equals("find")) { return find(); }
			if (getLookAhead().val.equals("insert")) { return insert(); }
			if (getLookAhead().val.equals("remove")) { return remove(); }
			if (getLookAhead().val.equals("list")) { return list(); }
			if (getLookAhead().val.equals("show")) { return show(); }
		}
		return factory.newNullCommand("No such command");
	}

	private Command login() {
		match(SSType.NAME, "login");
		String username = name();
		match(SSType.NAME, "with");
		String password = name();
		return factory.newLoginCommand(username, password);
	}

	private Command use() {
		match(SSType.NAME, "use");
		String databaseName = name();
		Database database = factory.newDatabase(databaseName);
		processor.setDatabase(database);
		return factory.newUseCommand(databaseName);
	}

	private Command show() {
		match(SSType.NAME, "show");
		String spaceName = "";
		if (!getLookAhead().type.equals(SSType.EOF_TYPE)) {
			spaceName = name();
		}
		String vectorName = "";
		if (getLookAhead().type.equals(SSType.DOT)) {
			match(SSType.DOT);
			vectorName = name();
		}
		return factory.newShowCommand(spaceName, vectorName);
	}

	private Command list() {
		match(SSType.NAME, "list");
		return factory.newListCommand();
	}

	private Command insert() {
		match(SSType.NAME, "insert");
		String vectorName = name();
		match(SSType.EQUALS);
		SSVector v = vector(vectorName);
		match(SSType.NAME, "into");
		String tableName = name();
		return factory.newInsertCommand(tableName, v);
	}
	
	private Command remove() {
		match(SSType.NAME, "remove");
		String vectorName = name();
		match(SSType.NAME, "from");
		String tableName = name();
		return factory.newRemoveCommand(tableName, vectorName);
	}

	private Command find() {
		match(SSType.NAME, "find");
		int k = integer();
		match(SSType.NAME, "nearest");
		match(SSType.NAME, "to");
		SSVector v;
		if (getLookAhead().type.equals(SSType.LBRACK)) {
			v = vector("");
			match(SSType.NAME, "in");
			String spaceName = name();
			return factory.newFindCommand(spaceName, k, v);
		}else{
			String querySpaceName = name();
			match(SSType.DOT);
			String queryVectorName = name();
			match(SSType.NAME, "in");
			String spaceName = name();
			return factory.newFindVectorCommand(spaceName, k, querySpaceName, queryVectorName);
		}
	}

	private Command create() {
		match(SSType.NAME, "create");
		match(SSType.NAME, "space");
		String spaceName = name();
		match(SSType.NAME, "with");
		match(SSType.NAME, "dimensionality");
		int dimensionality = integer();
		return factory.newCreateCommand(spaceName, dimensionality);
	}

	private Command drop() {
		match(SSType.NAME, "drop");
		String spaceName = name();
		return factory.newDropCommand(spaceName);
	}

	private String name() {
		String name = getLookAhead().val;
		match(SSType.NAME);
		return name;
	}

	private int integer() {
		int integer = Integer.parseInt(getLookAhead().val);
		match(SSType.NUMBER);
		return integer;
	}

	private double real() {
		double real = Double.parseDouble(getLookAhead().val);
		match(SSType.NUMBER);
		return real;
	}

	private SSVector vector(String name) {
		match(SSType.LBRACK);
		List<Double> vals = new ArrayList<Double>();
		while(getLookAhead().type.equals(SSType.NUMBER)){
			vals.add(real());
			if (getLookAhead().type.equals(SSType.COMMA)) {
				match(SSType.COMMA);
			}
		}
		match(SSType.RBRACK);
		return Vectors.createVector(name, vals);
	}

}
