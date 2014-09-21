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
package com.vectorization.driver.builders;

import com.vectorization.core.SSVector;

public class FindBuilder {
	
	private String statement;
	
	public FindBuilder(String string) {
		statement = string;
	}

	public FindBuilder2 nearestTo(String id){
		return new FindBuilder2(statement + " nearest to " + id);
	}
	
	public FindBuilder2 nearestTo(SSVector v){
		return new FindBuilder2(statement + " nearest to " + v.toString());
	}
}
