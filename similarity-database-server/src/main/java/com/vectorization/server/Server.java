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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	
	private static final Logger log = LoggerFactory.getLogger(Server.class);

	private ServerSocket serverSocket;
	private ExecutorService threadPool;
	private volatile boolean keepProcessing = true;
	

	public Server(int port) {
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
	
	private void accept() throws IOException{
		final Socket clientSocket = serverSocket.accept();
		submit(createHandler(clientSocket));
	}
	
	private Runnable createHandler(final Socket clientSocket){
		return new Runnable() {

			public void run() {
				try {
					Processor p = new Processor(clientSocket.getOutputStream());
					p.process(clientSocket.getInputStream());
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		};
	}
	
	private void submit(Runnable r){
		threadPool.submit(r);
	}


	public static void main(String[] args) {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:users.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		new Server(4567);
	}

}
