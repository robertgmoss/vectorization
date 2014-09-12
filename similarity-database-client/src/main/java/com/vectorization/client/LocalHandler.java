package com.vectorization.client;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class LocalHandler extends AbstractHandler {

	private Set<String> keywords = new LinkedHashSet<String>();

	public LocalHandler(AbstractHandler successor) {
		super(successor);
		keywords.addAll(Arrays.asList("quit", "exit"));
	}

	@Override
	public String processRequest(String command) {
		if (!keywords.contains(command.toLowerCase())) return forward(command);
		// handle locally
		if (command.equals("quit") || command.equals("exit")) quit();
		String result = command;
		return result;
	}

	private void quit() {
		System.out.println("Bye!");
		System.exit(0);
	}

}
