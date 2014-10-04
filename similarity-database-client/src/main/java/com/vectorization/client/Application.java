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
package com.vectorization.client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vectorization.driver.Vectorization;

public class Application extends Vectorization {

	public static final String VERSION = "0.0.4-SNAPSHOT";

	private CommandLineParser commandLineParser;
	private ClientConnection client;

	public static void printVersion() {
		StringBuilder sb = new StringBuilder();
		sb.append("vectorization Version " + VERSION + " for JVM.\n");
		sb.append("Copyright (c) 2014, Robert Moss. All rights reserved.\n");
		System.out.println(sb);
	}

	@Inject
	public Application(CommandLineParser command1Lineparser) {
		this.commandLineParser = command1Lineparser;
		this.client = getConnection();
	}

	@Override
	public ClientConnection getConnection() {
		return new ClientConnection();
	}

	private static Options createOptions() {
		Options options = new Options();
		options.addOption("D", "database", true, "Database to use.");
		options.addOption("h", "host", true, "Connect to host.");
		OptionBuilder.withLongOpt("help");
		OptionBuilder.withDescription("Print this message");
		Option help = OptionBuilder.create();
		options.addOption(help);
		Option passwordOption = new Option("p", "password", true,
				"Password to use when connecting "
						+ "to server.  If password is not "
						+ "given it's asked from the tty.");
		passwordOption.setOptionalArg(true);
		options.addOption(passwordOption);
		options.addOption("P", "port", true,
				"Port number to use for connection");
		options.addOption("u", "user", true,
				"User for login if not current user.");
		options.addOption("V", "version", false,
				"Output version information and exit.");
		return options;
	}

	private void printHelpAndExit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar vectorization-client-" + VERSION
				+ ".jar", options, true);
		System.exit(0);
	}

	private void printVersionAndExit() {
		printVersion();
		System.exit(0);
	}
	
	private void connect(String host, int port){
		client.setAddress(host);
		client.setPort(port);
		client.connect();
	}
	
	private void login(String username, String password){
		if (password == null) {
			password = client.promptForPassword();
		}
		client.login(username, password);
	}

	public void run(String[] args) {
		Options options = createOptions();
		try {
			CommandLine line = commandLineParser.parse(options, args);
			if (line.hasOption("V")) {
				printVersionAndExit();
			}
			
			if (line.hasOption("help")) {
				printHelpAndExit(options);
			}	
			
			String host = DEFAULT_ADDRESS;
			if (line.hasOption("h")) {
				host = line.getOptionValue("h");
			}
			
			int port = DEFAULT_PORT;
			if (line.hasOption("P")) {
				port = Integer.parseInt(line.getOptionValue("P"));
			}
			connect(host, port);
			
			if (line.hasOption("u") && line.hasOption("p")) {
				String username = line.getOptionValue("u");
				String password = line.getOptionValue("p");
				login(username, password);
			}
	
			if (line.hasOption("D")) {
				client.useDatabase(line.getOptionValue("D"));
			}
	
			client.startPrompt();
			client.close();
	
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AppInjector());
		Application app = injector.getInstance(Application.class);
		app.run(args);
	}

}
