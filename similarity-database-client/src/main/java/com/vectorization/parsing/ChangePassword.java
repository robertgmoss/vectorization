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
