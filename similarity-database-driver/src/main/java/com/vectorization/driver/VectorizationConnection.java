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
package com.vectorization.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.vectorization.core.VectorizationException;
import com.vectorization.util.IO;

public class VectorizationConnection {
	
	private Handler handler;
	private String address;
	private int port;
	private Socket socket;
	private PrintWriter requestWriter;
	private BufferedReader responseReader;
	
	public VectorizationConnection(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public Statement createStatement(){
		return new Statement(handler);
	}
	
	public void connect() {
		connect(address, port);
	}
	
	public void connect(String address, int port) {
		try {
			handler = createHandler(address, port);
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
	}
	
	protected Handler createHandler(BufferedReader in, PrintWriter out) {
		return new RemoteHandler(in, out);
	}
	
	private Handler createHandler(String address, int port)
			throws UnknownHostException, IOException {
		socket = IO.createSocket(address, port);
		requestWriter = IO.createPrintWriter(socket.getOutputStream());
		responseReader = IO.createBufferedReader(socket.getInputStream());
		return createHandler(responseReader, requestWriter);
	}
	
	public void close() {
		try {
			if (responseReader != null)
				responseReader.close();
			if (requestWriter != null)
				requestWriter.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
