package com.vectorization.server.master;

import java.io.BufferedReader;
import java.io.PrintWriter;

import com.vectorization.driver.VectorizationConnection;
import com.vectorization.driver.Handler;
import com.vectorization.server.master.network.Network;

public class MasterConnection extends VectorizationConnection{

	private Network network;

	public MasterConnection(String address, int port, Network network) {
		super(address, port);
		this.network = network;
	}
	
	@Override
	protected Handler createHandler(BufferedReader in, PrintWriter out) {
		return new MasterHandler(super.createHandler(in, out), network);
	}

}
