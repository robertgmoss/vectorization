package com.vectorization.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.vectorization.util.IO;

public class Client {

	private Handler handler;

	public Client(String database, String address, int port) {
		try {
			handler = createHandler(address, port);
			printWelcome();
			useDatabase(database);
			processInput();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processInput() throws IOException {
		BufferedReader stdIn = IO.createBufferedReader(System.in);
		processInput(stdIn);
	}

	private void processInput(BufferedReader stdIn) throws IOException {
		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
			sendRequest(userInput);
			prompt();
		}
	}

	private void useDatabase(String database) {
		sendRequest("use " + database);
		prompt();
	}

	private Handler createHandler(BufferedReader in, PrintWriter out) {
		return new LocalHandler(new RemoteHandler(in, out));
	}

	private Handler createHandler(String address, int port)
			throws UnknownHostException, IOException {
		Socket socket = IO.createSocket(address, port);
		PrintWriter out = IO.createPrintWriter(socket.getOutputStream());
		BufferedReader in = IO.createBufferedReader(socket.getInputStream());
		return createHandler(in, out);
	}

	private void printWelcome() {
		System.out.println("Welcome to similarity-database client");
		System.out.println("copyright Robert Moss, all rights reserved");
		System.out.println();
	}

	private void prompt() {
		System.out.print("> ");
	}

	private void sendRequest(String request) {
		System.out.println(handler.processRequest(request));
	}

	public static void main(String[] args) {
		new Client("Test", "localhost", 4567);
	}

}
