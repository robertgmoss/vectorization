package com.vectorization.server.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectorization.driver.AbstractHandler;
import com.vectorization.driver.Handler;
import com.vectorization.parsing.Parser;
import com.vectorization.parsing.ServerLexer;
import com.vectorization.server.master.command.MasterCommand;
import com.vectorization.server.master.network.Network;
import com.vectorization.server.master.parsing.MasterParser;

public class MasterHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(MasterHandler.class);
	private Network network;

	public MasterHandler(Handler handler, Network network) {
		super(handler);
		this.network = network;
	}

	@Override
	public String processRequest(final String command) {
		MasterCommand forward = new MasterCommand() {

			public String execute(Network network) {
				return forward(command);
			}
		};
		Parser<MasterCommand> p = new MasterParser(new ServerLexer(command), forward);
		return p.parse().execute(network);
	}

}
