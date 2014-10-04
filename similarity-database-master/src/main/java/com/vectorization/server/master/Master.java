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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.vectorization.server.ProcessorFactory;
import com.vectorization.server.Server;

public class Master implements Server {

	private static final Logger log = LoggerFactory.getLogger(Master.class);

	private ServerSocket serverSocket;
	private ExecutorService threadPool;
	private volatile boolean keepProcessing = true;

	private ProcessorFactory processorFactory;

	private int port = 4545;

	@Inject
	public Master(ProcessorFactory processorFactory) {
		this.processorFactory = processorFactory;
	}

	public void run() {
		log.info("Starting server on port: " + port);
		try {
			serverSocket = new ServerSocket(port);
			threadPool = Executors.newCachedThreadPool();
			while (keepProcessing) {
				accept();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutDown() {
		keepProcessing = false;
	}

	private void accept() throws IOException {
		final Socket clientSocket = serverSocket.accept();
		log.info("accepting connection: " + clientSocket.getInetAddress());
		submit(createHandler(clientSocket));
	}

	private Runnable createHandler(final Socket clientSocket) throws IOException {
			return processorFactory.create(clientSocket.getOutputStream(),
					clientSocket.getInputStream());
	}

	private void submit(Runnable r) {
		threadPool.submit(r);
	}

	public void setPort(int port) {
		this.port = port;
	}

}
