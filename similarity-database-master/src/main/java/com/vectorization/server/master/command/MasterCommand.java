package com.vectorization.server.master.command;

import com.vectorization.server.master.network.Network;

public interface MasterCommand {

	String execute(Network network);
}
