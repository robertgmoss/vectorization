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
import com.vectorization.core.VectorizationException;

public class StatementBuilders {
	
	public static AddUserBuilder addUser(String user){
		return new AddUserBuilder("add user " + user);
	}
	
	public static ChangePasswordBuilder changePassword(String password){
		return new ChangePasswordBuilder("change password ");
	}
	
	public static CreateBuilder create(String spaceName){
		if(spaceName == null || spaceName.equals("")) throw new VectorizationException("Space name cannot be null or empty");
		return new CreateBuilder("create space " + spaceName);
	}
	
	public static Builder drop(String spaceName){
		return new Builder("drop " + spaceName);
	}
	
	public static FindBuilder find(int number){
		return new FindBuilder("find " + number);
	}
	
	public static GrantBuilder grant(String... command){
		return new GrantBuilder("grant ", command);
	}

	public static InsertBuilder insert(Vector... vectors){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < vectors.length; i++){
			sb.append(vectors[i]);
			if(i != vectors.length - 1){
				sb.append(", ");
			}
		}
		return new InsertBuilder("insert " + sb);
	}
	
	public static Builder list(){
		return new Builder("list");
	}
	
	public static LoginBuilder login(String user){
		return new LoginBuilder("login " + user);
	}
	
	public static RemoveBuilder remove(String... vectors){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < vectors.length; i++){
			sb.append(vectors[i]);
			if(i != vectors.length - 1){
				sb.append(", ");
			}
		}
		return new RemoveBuilder("remove " + sb);
	}

	public static QueryBuilder show(String space, String id){
		return new QueryBuilder("show " + space + "." + id);
	}
	
	public static QueryBuilder show(String space){
		return new QueryBuilder("show " + space);
	}

	public static Builder use(String database){
		return new Builder("use " + database);
	}

}