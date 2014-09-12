package com.vectorization.client;

public abstract class AbstractHandler implements Handler {

	public static final AbstractHandler NULL_HANDLER = new AbstractHandler(null) { };
	private AbstractHandler successor;

	public AbstractHandler(AbstractHandler successor) {
		this.successor = successor;
	}

	public AbstractHandler() {
		this(NULL_HANDLER);
	}

	public String processRequest(String command) {
		return command;

	}

	protected String forward(String command) {
		return this.successor.processRequest(command);
	}

}
