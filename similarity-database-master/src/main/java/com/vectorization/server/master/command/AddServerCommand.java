package com.vectorization.server.master.command;

import java.sql.SQLException;

import com.vectorization.core.VectorizationException;
import com.vectorization.server.master.network.Network;

public class AddServerCommand implements MasterCommand {

	private String id;
	private String location;
	private int port;

	public AddServerCommand(String name, String address, int port) {
		this.id = name;
		this.location = address;
		this.port = port;
	}

	public String execute(Network network) {
		try {
			network.insertServer(id, location, port);
			return "server added";
		} catch (SQLException e) {
			throw new VectorizationException("unable to add server: "+e.getMessage());
		}
	}

}
