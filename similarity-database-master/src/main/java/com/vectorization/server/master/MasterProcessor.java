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
package com.vectorization.server.master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vectorization.core.database.Database;
import com.vectorization.parsing.Command;
import com.vectorization.parsing.Parser;
import com.vectorization.parsing.ParserFactory;
import com.vectorization.server.Processor;
import com.vectorization.util.IO;

public class MasterProcessor implements Processor {

	private static final Logger log = LoggerFactory
			.getLogger(MasterProcessor.class);

	private Database database;
	private PrintWriter out;
	private BufferedReader in;
	private ParserFactory parserFactory;

	@Inject
	public MasterProcessor(ParserFactory parserFactory, @Assisted OutputStream os,
			@Assisted InputStream is) throws IOException {
		this.parserFactory = parserFactory;
		this.in = IO.createBufferedReader(is);
		this.out = IO.createPrintWriter(os);
	}

	private void process(BufferedReader in) throws IOException {
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			out.println(process(inputLine));
		}
	}

	private String process(String inputLine) {
		String output = "";
		try {
			output = parseLine(inputLine).execute(database);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			output = e.getMessage();
		}
		return output;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	private Command parseLine(String input) {
		Parser<Command> p = parserFactory.create(this, input);
		return p.parse();
	}

	public void close() throws IOException {
		out.close();
		in.close();
	}

	public void run() {
		try {
			process(in);
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
