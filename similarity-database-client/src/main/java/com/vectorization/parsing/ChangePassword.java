package com.vectorization.parsing;

import java.io.BufferedReader;

import com.vectorization.core.VectorizationException;
import com.vectorization.driver.Handler;

public class ChangePassword implements ClientCommand {

	public String execute(Handler handler, BufferedReader stdIn) {
		try {
			System.out.println("type password:");
			String first = stdIn.readLine();
			System.out.println("re-type password:");
			String second = stdIn.readLine();

			if (!first.equals(second))
				throw new VectorizationException("Passwords do not match");
			return handler.processRequest("change password to " + first);
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
	}
}
