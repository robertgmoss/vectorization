package com.vectorization.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.vectorization.core.Database;
import com.vectorization.core.SSException;
import com.vectorization.server.lexer.SSLexer;
import com.vectorization.server.parser.Parser;
import com.vectorization.server.parser.SSParser;
import com.vectorization.util.IO;

public class Processor {
	
	private Database database;
	private PrintWriter out;

	public Processor(PrintWriter out){
		this.out = out;
	}
	
	public Processor(OutputStream os) throws IOException{
		this(IO.createPrintWriter(os));
	}
	
	public void process(InputStream in)
			throws IOException {
		process(IO.createBufferedReader(in));
	}

	public void process(BufferedReader in)
			throws IOException {
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			out.println(process(inputLine));
		}
		in.close();
	}
	
	private String process(String inputLine){
		String output = "";
		try {
			output = parseLine(inputLine).execute(database);
		} catch (Exception e) {
			output = e.getMessage();
		}
		return output;
	}
	
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	private Command parseLine(String input) {
		Parser p = new SSParser(this, new SSLexer(input));
		return p.parse();
	}
	
	public void close() throws IOException{
		out.close();
	}
}
