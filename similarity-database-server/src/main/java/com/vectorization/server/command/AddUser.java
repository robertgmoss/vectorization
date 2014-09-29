package com.vectorization.server.command;

import java.sql.SQLException;

import com.vectorization.core.VectorizationException;
import com.vectorization.core.database.Database;
import com.vectorization.server.security.Security;

public class AddUser extends AbstractCommand{
	
	private String username;
	private String password;
	private Security security;

	public AddUser(Security security, String username, String password) {
		this.security = security;
		this.username = username;
		this.password = password;
	}

	@Override
	public String execute(Database database) {
		try {
			security.insertUser(username, password);
			security.insertRole(username);
			security.insertUserRole(username, username);
			security.insertUserRole(username, "user");
			return "user added";
		} catch (SQLException e) {
			throw new VectorizationException("can't add user: "+ e.getMessage());
		}
	}

	public String getPermissionLevel() {
		return "adduser";
	}

}
