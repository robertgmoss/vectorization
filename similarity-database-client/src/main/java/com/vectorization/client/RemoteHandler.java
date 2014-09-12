package com.vectorization.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RemoteHandler extends AbstractHandler {

	private PrintWriter out;
	private BufferedReader in;

	public RemoteHandler(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public String processRequest(String command) {
		// now assumes all commands are handled by the server
		// System.out.println("processing command:" + command);
		out.println(command);
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(in.readLine());
			while (in.ready()) {
				sb.append("\n" + in.readLine());
			}
			return sb.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
