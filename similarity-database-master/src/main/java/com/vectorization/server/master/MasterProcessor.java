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
import com.vectorization.parsing.Parser;
import com.vectorization.parsing.ParserFactory;
import com.vectorization.parsing.ServerCommand;
import com.vectorization.parsing.ServerLexer;
import com.vectorization.server.Processor;
import com.vectorization.server.master.command.MasterCommand;
import com.vectorization.server.master.network.Network;
import com.vectorization.server.master.parsing.MasterParser;
import com.vectorization.util.IO;

public class MasterProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(MasterProcessor.class);

	private Database database;
	private PrintWriter out;
	private BufferedReader in;
	private ParserFactory parserFactory;
//	private Handler handler;
	private Network network;

	@Inject
	public MasterProcessor(ParserFactory parserFactory, Network network, @Assisted OutputStream os,
			@Assisted InputStream is) throws IOException {
		this.parserFactory = parserFactory;
		this.network = network;
		this.in = IO.createBufferedReader(is);
		this.out = IO.createPrintWriter(os);
//		this.handler = createHandler();
	}
//
//	private Handler createHandler() {
//		return new MasterServerHandler(this, parserFactory);
//	}

	private void process(BufferedReader in) throws IOException {
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			out.println(processLine(inputLine));
		}
	}
	
	private String processLine(String inputLine){
		String output;
		try{
			output = processRequest(inputLine);
		}catch (Exception e) {
			log.error(e.getMessage());
			output = e.getMessage();
		}
		return output;
	}

	public void setDatabase(Database database) {
		this.database = database;
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

	public String processRequest(final String command) {
		MasterCommand forward = new MasterCommand() {

			public String execute(Network network) {
				return forward(command);
			}
		};
		Parser<MasterCommand> p = new MasterParser(new ServerLexer(command), forward);
		return p.parse().execute(network);
	}
	
	private String forward(String command) {
		Parser<ServerCommand> parser = parserFactory.create(this, command);
		return parser.parse().execute(database);
	}
}
