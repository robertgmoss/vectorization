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
package com.vectorization.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Provides convenience methods for input/output.
 * 
 * @author Robert Moss
 *
 */
public class IO {
	
	public static PrintWriter createPrintWriter(OutputStream os) throws IOException {
		return new PrintWriter(os, true);
	}
	
	public static Socket createSocket(String address, int port)
			throws UnknownHostException, IOException {
		return new Socket(address, port);
	}
	
	public static BufferedReader createBufferedReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}

}
