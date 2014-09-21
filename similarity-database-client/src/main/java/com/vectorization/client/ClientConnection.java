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

import com.vectorization.core.SSException;
import com.vectorization.driver.Connection;
import com.vectorization.driver.Handler;
import com.vectorization.driver.Vectorization;
import com.vectorization.util.IO;

public class ClientConnection extends Connection{

	private BufferedReader stdIn = IO.createBufferedReader(System.in);

	public ClientConnection(String address, int port) {
		super(address, port);
		printWelcome();
	}

	public ClientConnection() {
		this(Vectorization.DEFAULT_ADDRESS, Vectorization.DEFAULT_PORT);
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
		if(stdIn == null) System.out.println("DEBUG: stdin is null");
		while ((userInput = stdIn.readLine()) != null) {
			sendRequest(userInput);
			prompt();
		}
	}

	public void login(String username, String password) {
		sendRequest("login " + username + " with " + password);
	}

	protected Handler createHandler(BufferedReader in, PrintWriter out) {
		return new LocalHandler(super.createHandler(in, out));
	}

	private void printWelcome() {
		Application.printVersion();
		System.out.println("Welcome to vectorization.");
		System.out.println("Type 'help' for help.");
		System.out.println();
	}

	private void prompt() {
		System.out.print("> ");
	}

	public String promptForPassword() {
		System.out.println("password: ");
		try {
			return stdIn.readLine();
		} catch (IOException e) {
			throw new SSException(e);
		}
	}

	public void useDatabase(String database) {
		sendRequest("use " + database);
	}

	private void sendRequest(String request) {
		System.out.println(createStatement().execute(request));
	}

	public void close() {
		try {
			super.close();
			if (stdIn != null)
				stdIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
