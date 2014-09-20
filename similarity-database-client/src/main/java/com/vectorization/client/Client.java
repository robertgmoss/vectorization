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
package com.vectorization.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.vectorization.core.SSException;
import com.vectorization.util.IO;

public class Client {

	private Handler handler;
	private String address;
	private int port;
	private BufferedReader stdIn;
	private Socket socket;
	private PrintWriter requestWriter;
	private BufferedReader responseReader;

	public Client(String address, int port) {
		this.address = address;
		this.port = port;
		printWelcome();
		stdIn = IO.createBufferedReader(System.in);
	}

	public void connect() {
		try {
			handler = createHandler(address, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startPrompt() {
		try {
			processInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processInput() throws IOException {
		processInput(stdIn);
	}

	private void processInput(BufferedReader stdIn) throws IOException {
		String userInput;
		prompt();
		while ((userInput = stdIn.readLine()) != null) {
			sendRequest(userInput);
			prompt();
		}
	}

	public void login(String username, String password) {
		sendRequest("login " + username + " with " + password);
	}

	private Handler createHandler(BufferedReader in, PrintWriter out) {
		return new LocalHandler(new RemoteHandler(in, out));
	}

	private Handler createHandler(String address, int port)
			throws UnknownHostException, IOException {
		socket = IO.createSocket(address, port);
		requestWriter = IO.createPrintWriter(socket.getOutputStream());
		responseReader = IO.createBufferedReader(socket.getInputStream());
		return createHandler(responseReader, requestWriter);
	}

	private void printWelcome() {
		printVersion();
		System.out.println("Welcome to vectorization.");
		System.out.println("Type 'help' for help.");
		System.out.println();
	}

	private void prompt() {
		System.out.print("> ");
	}

	private String promptForPassword() {
		System.out.println("password: ");
		try {
			return stdIn.readLine();
		} catch (IOException e) {
			throw new SSException(e);
		}
	}

	private void useDatabase(String database) {
		sendRequest("use " + database);
	}

	private void sendRequest(String request) {
		try {
			System.out.println(handler.processRequest(request));
		} catch (SSException e) {
			System.out.println(e.getMessage());
		}
	}

	public void close() {
		try {
			if (stdIn != null)
				stdIn.close();
			if (socket != null)
				socket.close();
			if (requestWriter != null)
				requestWriter.close();
			if (responseReader != null)
				responseReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		options.addOption("D", "database", true, "Database to use.");
		options.addOption("h", "host", true, "Connect to host.");
		Option passwordOption = new Option("p", "password", true,
				"Password to use when connecting to server. "
						+ "If password is not given it's asked from the tty.");
		passwordOption.setOptionalArg(true);
		options.addOption(passwordOption);
		options.addOption("P", "port", true, "Port number to use for connection");
		options.addOption("u", "user", true, "User for login if not current user.");
		options.addOption("V", "version", false, "Output version information and exit.");
		
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("V")) {
				printVersion();
				System.exit(0);
			}
			
			
			String host = "localhost";
			if (line.hasOption("h")) {
				host = line.getOptionValue("h");
			}
			int port = 4567;
			if(line.hasOption("P")){
				port = Integer.parseInt(line.getOptionValue("P"));
			}
			Client client = new Client(host, port);
			client.connect();
			if (line.hasOption("u") && line.hasOption("p")) {
				String username = line.getOptionValue("u");
				String password = line.getOptionValue("p");
				if (password == null) {
					password = client.promptForPassword();
				}
				client.login(username, password);
			}
			
			if(line.hasOption("D")){
				client.useDatabase(line.getOptionValue("D"));
			}

			client.startPrompt();
			client.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static void printVersion() {
		StringBuilder sb = new StringBuilder();
		sb.append("vectorization Version 0.0.3 for JVM.\n");
		sb.append("Copyright (c) 2014, Robert Moss. All rights reserved.\n");
		System.out.println(sb);
	}

}
