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
