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

import java.io.BufferedReader;

import com.vectorization.driver.AbstractHandler;
import com.vectorization.driver.Handler;
import com.vectorization.parsing.ClientCommand;
import com.vectorization.parsing.ClientLexer;
import com.vectorization.parsing.ClientParser;
import com.vectorization.parsing.Parser;

public class LocalHandler extends AbstractHandler {

	private BufferedReader stdIn;

	public LocalHandler(BufferedReader stdIn, Handler handler) {
		super(handler);
		this.stdIn = stdIn;
		
	}

	@Override
	public String processRequest(final String command) {
		ClientCommand forward = new ClientCommand() {
			
			public String execute(Handler h, BufferedReader br) {
				return forward(command);
			}
		};
		
		Parser<ClientCommand> p = new ClientParser(new ClientLexer(command), forward);
		return p.parse().execute(this, stdIn);
	}

}
