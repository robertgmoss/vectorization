package com.vectorization.server.master.network;

import java.sql.SQLException;

import com.vectorization.driver.VectorizationConnection;

public interface Network {

	void insertServer(String id, String location, int port) throws SQLException;
	
	void insertDatabase(String databaseName) throws SQLException;
	
	void insertDatabaseSpace(String databaseName, String serverId, String spaceName) throws SQLException;

	VectorizationConnection getConnection(String serverId);

	Iterable<String> getSpaces(String name) throws SQLException;

	Iterable<VectorizationConnection> getConnections(String databaseName, String space) throws SQLException;

	String getRandomServer();

	void drop(String name, String space) throws SQLException;
}
