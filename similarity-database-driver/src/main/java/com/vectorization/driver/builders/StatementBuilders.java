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

public class StatementBuilders {
	
	public static Builder use(String database){
		return new Builder("use " + database);
	}
	
	public static CreateBuilder create(String spaceName){
		return new CreateBuilder("create space " + spaceName);
	}
	
	public static Builder drop(String spaceName){
		return new Builder("drop " + spaceName);
	}
	
	public static FindBuilder find(int number){
		return new FindBuilder("find " + number);
	}
	
	public static InsertBuilder insert(String id, SSVector v){
		return new InsertBuilder("insert " + id + "=" + v);
	}
	
	public static Builder list(){
		return new Builder("list");
	}
	
	public static Builder show(String space, String id){
		return new Builder("show " + space + "." + id);
	}
	
	public static Builder show(String space){
		return new Builder("show " + space);
	}
	
	public static RemoveBuilder remove(String id){
		return new RemoveBuilder("remove " + id);
	}

}