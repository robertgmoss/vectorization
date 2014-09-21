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
package com.vectorization.driver;

import com.vectorization.core.SSVector;
import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.collection.Space;

public class Statement {
	
	private Handler handler;

	public Statement(Handler handler) {
		this.handler = handler;
	}

	/**
	 * Assumes the type of query passed will result in a space being returned.
	 * @param query
	 * @return
	 */
	public SSCollection<SSVector> executeQuery(String query){
		String result = execute(query);
		System.out.println(result);
		// do something with result
		return new Space<SSVector>(0);
	}
	
	public String execute(String statement){
		return handler.processRequest(statement);
	}
	
	public SSCollection<SSVector> getResult(){
		return null;
	}

}
