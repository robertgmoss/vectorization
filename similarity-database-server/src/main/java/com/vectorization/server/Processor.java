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
package com.vectorization.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.vectorization.core.Database;
import com.vectorization.parsing.Command;
import com.vectorization.parsing.Parser;
import com.vectorization.parsing.ServerLexer;
import com.vectorization.parsing.ServerParser;
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
		Parser<Command> p = new ServerParser(this, new ServerLexer(input));
		return p.parse();
	}
	
	public void close() throws IOException{
		out.close();
	}
}
