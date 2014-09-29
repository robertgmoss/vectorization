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

import com.vectorization.core.Vector;

public class FindBuilder extends StatementBuilder{
	
	public static class FindBuilderIntermediate extends StatementBuilder{
		
		public FindBuilderIntermediate(String string) {
			super(string);
		}

		public QueryBuilder in(String id){
			return new QueryBuilder(getStatement() + " in " + id);
		}
	}
	
	public FindBuilder(String string) {
		super(string);
	}

	public FindBuilderIntermediate nearestTo(String id){
		return new FindBuilderIntermediate(getStatement() + " nearest to " + id);
	}
	
	public FindBuilderIntermediate nearestTo(Vector v){
		return new FindBuilderIntermediate(getStatement() + " nearest to " + v.toString());
	}
}
