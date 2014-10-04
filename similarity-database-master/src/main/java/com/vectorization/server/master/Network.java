package com.vectorization.server.master;

import java.sql.SQLException;

import com.vectorization.driver.Connection;

public interface Network {

	void insertServer(String id, String location, int port) throws SQLException;
	
	void insertDatabase(String databaseName) throws SQLException;
	
	void insertDatabaseSpace(String databaseName, String serverId, String spaceName) throws SQLException;

	Connection getConnection(String serverId);

	Iterable<String> getSpaces(String name) throws SQLException;

	Iterable<Connection> getConnections(String databaseName, String space) throws SQLException;
}
