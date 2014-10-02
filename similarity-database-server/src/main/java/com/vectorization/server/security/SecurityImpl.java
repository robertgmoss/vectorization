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
package com.vectorization.server.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vectorization.core.VectorizationException;

@Singleton
public class SecurityImpl implements Security {

	private static final String CREATE_USERS = "create table users ("
			+ "    username varchar(255) primary key,"
			+ "    password varchar(255) not null" + ")";

	private static final String CREATE_ROLES = "create table roles ("
			+ "    role_name varchar(255) primary key)";

	private static final String CREATE_USER_ROLES = "create table user_roles ("
			+ "    username varchar(255) not null,"
			+ "    role_name varchar(255) not null,"
			+ "    constraint user_roles_uq unique ( username, role_name )"
			+ ")";

	private static final String CREATE_ROLES_PERMISSIONS = "create table roles_permissions ("
			+ "    role_name varchar(255) not null,"
			+ "    permission varchar(255) not null,"
			+ "    primary key (role_name, permission)" + ")";

	private static final String INSERT_USER = "insert into users values (?, ?)";

	private static final String INSERT_ROLE = "insert into roles values (?)";

	private static final String INSERT_USER_ROLE = "insert into user_roles values (?, ?)";

	private static final String INSERT_ROLE_PERMISSION = "insert into roles_permissions values (?, ?)";

	private static final String UPDATE_PASSWORD = "update users set password=? where username=?";

	private static final Logger log = LoggerFactory.getLogger(SecurityImpl.class);

	private DataSource dataSource;
	
	@Inject
	public SecurityImpl(DataSource dataSource){
		this.dataSource = dataSource;
		initDatabaseLoggingException();
	}

	private void initDatabaseLoggingException() {
		try {
			initDatabase();
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
	}

	private void initDatabase() throws SQLException {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			statement.execute(CREATE_USERS);
			statement.execute(CREATE_ROLES);
			statement.execute(CREATE_USER_ROLES);
			statement.execute(CREATE_ROLES_PERMISSIONS);
			createAdminUser(conn);
			createUserRole(conn);
		} finally {
			if (statement != null)
				statement.close();
			if (conn != null)
				conn.close();
		}
	}
	
	private void createAdminUser(Connection conn) throws SQLException{
		insertUser(conn, "admin", "admin");
		insertRole(conn, "admin");
		insertUserRole(conn, "admin", "admin");
		insertRolePermission(conn, "admin", "*");
	}
	
	private void createUserRole(Connection conn) throws SQLException{
		insertRole(conn, "user");
		insertRolePermission(conn, "user", "*:passwd");
	}

	public void insertUser(String username, String password)
			throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertUser(conn, username, password);
		} catch (SQLException e) {
			throw new VectorizationException("can't insert user: "+e);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private void insertUser(Connection conn, String username,
			String password) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_USER);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public void insertRole(String roleName) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertRole(conn, roleName);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	private void insertRole(Connection conn, String roleName)
			throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_ROLE);
			ps.setString(1, roleName);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public void insertUserRole(String username, String roleName)
			throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertUserRole(conn, username, roleName);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	private void insertUserRole(Connection conn, String username,
			String roleName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_USER_ROLE);
			ps.setString(1, username);
			ps.setString(2, roleName);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public void insertRolePermission(String roleName, String permission)
			throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertRolePermission(conn, roleName, permission);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	private void insertRolePermission(Connection conn, String roleName,
			String permission) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_ROLE_PERMISSION);
			ps.setString(1, roleName);
			ps.setString(2, permission);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public void removeRolePermission(String roleName, String permission) {
		// tricky because of wildcards
	}

	public void updatePassword(String username, String password) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			updatePassword(conn, username, password);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	private void updatePassword(Connection conn, String username,
			String password) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(UPDATE_PASSWORD);
			ps.setString(1, password);
			ps.setString(2, username);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}
}
