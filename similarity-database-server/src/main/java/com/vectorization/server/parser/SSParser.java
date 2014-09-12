package com.vectorization.server.parser;

import java.util.ArrayList;
import java.util.List;

import com.vectorization.server.Command;
import com.vectorization.server.Create;
import com.vectorization.server.Drop;
import com.vectorization.server.Find;
import com.vectorization.server.FindVector;
import com.vectorization.server.Insert;
import com.vectorization.server.Null;
import com.vectorization.server.Processor;
import com.vectorization.server.Select;
import com.vectorization.server.Show;
import com.vectorization.server.lexer.Lexer;
import com.vectorization.server.lexer.SSLexer.SSType;
import com.vectorization.core.Database;
import com.vectorization.core.SSVector;
import com.vectorization.core.Vectors;

public class SSParser extends Parser {

	private Processor processor;

	public SSParser(Processor processor, Lexer l) {
		super(l);
		this.processor = processor;
	}

	@Override
	public Command parse() {
		if (getLookAhead().type.equals(SSType.NAME)) {
			if (getLookAhead().val.equals("use")) { return use(); }
			if (getLookAhead().val.equals("create")) { return create(); }
			if (getLookAhead().val.equals("drop")) { return drop(); }
			if (getLookAhead().val.equals("find")) { return find(); }
			if (getLookAhead().val.equals("insert")) { return insert(); }
			if (getLookAhead().val.equals("list")) { return list(); }
			if (getLookAhead().val.equals("select")) { return select(); }
			if (getLookAhead().val.equals("show")) { return show(); }
		}
		return new Null("No such command");
	}

	private Command use() {
		match(SSType.NAME, "use");
		String databaseName = name();
		processor.setDatabase(new Database(databaseName));
		return new Use(databaseName);
	}

	private Command show() {
		match(SSType.NAME, "show");
		String tableName = "";
		if (!getLookAhead().type.equals(SSType.EOF_TYPE)) {
			tableName = name();
		}
		String vectorName = "";
		if (getLookAhead().type.equals(SSType.DOT)) {
			match(SSType.DOT);
			vectorName = name();
		}
		return new Show(tableName, vectorName);
	}

	private Command select() {
		match(SSType.NAME, "select");
		String tableName = name();
		return new Select(tableName);
	}

	private Command list() {
		match(SSType.NAME, "list");
		return new com.vectorization.server.List();
	}

	private Command insert() {
		match(SSType.NAME, "insert");
		String vectorName = name();
		match(SSType.EQUALS);
		SSVector v = vector(vectorName);
		match(SSType.NAME, "into");
		String tableName = name();
		return new Insert(v, tableName);
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
			String tableName = name();
			return new Find(k, v, tableName);
		}else{
			String tableName = name();
			match(SSType.DOT);
			String vectorName = name();
			match(SSType.NAME, "in");
			String space = name();
			return new FindVector(k, tableName, vectorName, space);
		}
		
		
	}

	private Command create() {
		match(SSType.NAME, "create");
		match(SSType.NAME, "space");
		String tableName = name();
		match(SSType.NAME, "with");
		match(SSType.NAME, "dimensionality");
		int dimensionality = integer();
		return new Create(tableName, dimensionality);
	}

	private Command drop() {
		match(SSType.NAME, "drop");
		String tableName = name();
		return new Drop(tableName);
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
