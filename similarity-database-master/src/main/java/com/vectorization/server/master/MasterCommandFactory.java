package com.vectorization.server.master;

import com.google.inject.Inject;
import com.vectorization.core.database.Database;
import com.vectorization.server.command.AbstractCommandFactory;

public class MasterCommandFactory extends AbstractCommandFactory{

	private Network network;

	@Inject
	public MasterCommandFactory(Network network) {
		this.network = network;
	}

	public Database newDatabase(String name) {
		return new DistributedDatabase(name, network);
	}
}
