package com.vectorization.server.security;

import java.sql.SQLException;

public class NullSecurity implements Security {

	public void insertUser(String username, String password)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void insertRole(String roleName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void insertUserRole(String username, String roleName)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void insertRolePermission(String roleName, String permission)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updatePassword(String username, String password)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

}
