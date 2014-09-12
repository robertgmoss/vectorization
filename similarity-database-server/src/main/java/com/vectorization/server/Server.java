package com.vectorization.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

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
					e.printStackTrace();
				}
			}
		};
	}
	
	private void submit(Runnable r){
		threadPool.submit(r);
	}


	public static void main(String[] args) {
		new Server(4567);
	}

}
