package com.vectorization.server.security;

import java.sql.SQLException;

public interface Security {

	void insertUser(String username, String password)
			throws SQLException;
	void insertRole(String roleName) throws SQLException;
	void insertUserRole(String username, String roleName)
			throws SQLException;
	void insertRolePermission(String roleName, String permission)
			throws SQLException;
	void updatePassword(String username, String password) throws SQLException;
}