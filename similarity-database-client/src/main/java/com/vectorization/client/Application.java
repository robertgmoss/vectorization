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
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.vectorization.driver.Vectorization;

public class Application extends Vectorization{
	
	@Override
	public ClientConnection getConnection() {
		return new ClientConnection();
	}
	
	private static Options createOptions(){
		Options options = new Options();
		options.addOption("D", "database",  true,  "Database to use.");
		options.addOption("h", "host",      true,  "Connect to host.");
		Option passwordOption = 
			   new Option("p", "password",  true,  "Password to use when connecting "
			   		                             + "to server.  If password is not "
			   		                             + "given it's asked from the tty.");
		passwordOption.setOptionalArg(true);
		options.addOption(passwordOption);
		options.addOption("P", "port",      true,  "Port number to use for connection");
		options.addOption("u", "user",      true,  "User for login if not current user.");
		options.addOption("V", "version",   false, "Output version information and exit.");
		return options;
	}
	
	public static void main(String[] args) {
		CommandLineParser parser = new PosixParser();
		Options options = createOptions();
		
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("V")) {
				printVersion();
				System.exit(0);
			}

			Application app = new Application();
			ClientConnection client = app.getConnection();
			String host = "localhost";
			if (line.hasOption("h")) {
				host = line.getOptionValue("h");
			}
			client.setAddress(host);
			int port = 4567;
			if(line.hasOption("P")){
				port = Integer.parseInt(line.getOptionValue("P"));
			}
			client.setPort(port);
			client.connect();
			if (line.hasOption("u") && line.hasOption("p")) {
				String username = line.getOptionValue("u");
				String password = line.getOptionValue("p");
				if (password == null) {
					password = client.promptForPassword();
				}
				client.login(username, password);
			}
			
			if(line.hasOption("D")){
				client.useDatabase(line.getOptionValue("D"));
			}

			client.startPrompt();
			client.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void printVersion() {
		StringBuilder sb = new StringBuilder();
		sb.append("vectorization Version 0.0.3 for JVM.\n");
		sb.append("Copyright (c) 2014, Robert Moss. All rights reserved.\n");
		System.out.println(sb);
	}

}
