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
package com.vectorization.server.master;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vectorization.server.Server;

public class Application {
	
	private static final Logger log = LoggerFactory
			.getLogger(Application.class);

	public static final String VERSION = "0.0.1-SNAPSHOT";
	
	private CommandLineParser commandLineParser;
	private Server server;

	@Inject
	public Application(CommandLineParser commandLineparser, Server server) {
		this.commandLineParser = commandLineparser;
		this.server = server;
	}

	public static void printVersion() {
		StringBuilder sb = new StringBuilder();
		sb.append("__     __           _                _             _    _\n");               
		sb.append("\\ \\   / /___   ___ | |_  ___   _ __ (_) ____ __ _ | |_ (_)  ___   _ __\n");  
		sb.append(" \\ \\ / // _ \\ / __|| __|/ _ \\ | '__|| ||_  // _` || __|| | / _ \\ | '_ \\\n"); 
		sb.append("  \\ V /|  __/| (__ | |_| (_) || |   | | / /| (_| || |_ | || (_) || | | |\n");
		sb.append("   \\_/  \\___| \\___| \\__|\\___/ |_|   |_|/___|\\__,_| \\__||_| \\___/ |_| |_|\n\n");
		sb.append("Vectorization-Master Version " + VERSION + " for JVM.\n");
		sb.append("Copyright (c) 2014-15, Robert Moss. All rights reserved.\n");
		System.out.println(sb);
	}
	
	private void printVersionAndExit(){
		printVersion();
		System.exit(0);
	}
	
	private void printHelpAndExit(Options options){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar vectorization-master-" + VERSION
				+ ".jar", options, true);
		System.exit(0);
	}

	private static Options createOptions() {
		Options options = new Options();
		OptionBuilder.withLongOpt("help");
		OptionBuilder.withDescription("Print this message");
		Option help = OptionBuilder.create();
		options.addOption(help);
		options.addOption("P", "port", true,
				"Port number to use for connection");
		options.addOption("V", "version", false,
				"Output version information and exit.");
		return options;
	}
	
	public void run(String[] args){
		Options options = createOptions();
		try {
			CommandLine line = commandLineParser.parse(options, args);
			if (line.hasOption("V")) {
				printVersionAndExit();
			}

			if (line.hasOption("help")) {
				printHelpAndExit(options);
			}

			if (line.hasOption("P")) {
				int port = Integer.parseInt(line.getOptionValue("P"));
				server.setPort(port);
			}
			
			printVersion();
			
			server.run();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		log.info("Server Stopping");
	}

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AppInjector());
		Application app = injector.getInstance(Application.class);
		app.run(args);
	}

}
