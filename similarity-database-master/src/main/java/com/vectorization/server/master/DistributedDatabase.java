package com.vectorization.server.master;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;
import com.vectorization.core.collection.SimpleVectorSpace;
import com.vectorization.core.collection.VectorSpace;
import com.vectorization.core.database.Database;
import com.vectorization.driver.Connection;
import com.vectorization.driver.builders.StatementBuilders;

public class DistributedDatabase implements Database {

	private static final Logger log = LoggerFactory
			.getLogger(DistributedDatabase.class);

	private final String databaseName;
	private Network network;

	public DistributedDatabase(String databaseName, Network network) {
		this.databaseName = databaseName;
		this.network = network;
		addToNetwork();
	}

	private void addToNetwork() {
		try {
			network.insertDatabase(getName());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void use(Connection connection) {
		String result = StatementBuilders.use(getName()).execute(connection);
		log.info(result);
	}

	public Database create(String spaceName, int dimensionality) {
		log.info("create");
		String serverId = "localhost"; // TODO get the best server
		try {
			network.insertDatabaseSpace(databaseName, serverId, spaceName);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
		Connection connection = network.getConnection(serverId);
		use(connection);
		String result = StatementBuilders.create(spaceName)
				.withDimensionality(dimensionality).execute(connection);
		log.info(result);
		return this;
	}

	public void drop(String space) {
		log.info("drop");
		try {
			Iterable<Connection> connections = network.getConnections(
					getName(), space);
			// TODO spawn threads
			for (Connection connection : connections) {
				use(connection);
				String result = StatementBuilders.drop(space).execute(
						connection);
				log.info(result);
			}
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	public Iterable<Vector> retrieveKnn(String spaceName, int k,
			Vector prototype) {
		log.info("retrieveKnn");
		try {
			VectorSpace result = null;
			Iterable<Connection> connections = network.getConnections(
					getName(), spaceName);
			// TODO spawn threads
			for (Connection connection : connections) {
				use(connection);
				VectorSpace found = StatementBuilders.find(k)
						.nearestTo(prototype).in(spaceName).execute(connection);
				if (result == null) {
					result = found;
				} else {
					result.insertAll(found);
				}
			}
			return result == null ? new SimpleVectorSpace(0) : result;
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}

	}

	public Database remove(String space, String... vectors) {
		log.info("remove");
		try {
			Iterable<Connection> connections = network.getConnections(
					getName(), space);
			// TODO spawn threads
			for (Connection connection : connections) {
				use(connection);
				String response = StatementBuilders.remove(vectors).from(space)
						.execute(connection);
				log.info(response);
			}
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
		return this;
	}

	public Database insert(String space, Vector... vectors) {
		log.info("insert");
		String serverId = "localhost"; // TODO get the server best suited.
		Connection connection = network.getConnection(serverId);
		use(connection);
		// TODO break up insert to balance load
		String result = StatementBuilders.insert(vectors).into(space)
				.execute(connection);
		// TODO keep count
		log.info(result);
		return null;
	}

	public Vector show(String spaceName, String vectorName) {
		log.info("show");
		try {
			Iterable<Connection> connections = network.getConnections(
					getName(), spaceName);
			VectorSpace response;
			// TODO spawn threads
			for (Connection connection : connections) {
				use(connection);
				response = StatementBuilders.show(spaceName, vectorName)
						.execute(connection);
				if (response.contains(vectorName))
					return response.get(vectorName);
			}
			throw new VectorizationException("No such vector");
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	public Iterable<Vector> show(String spaceName) {
		log.info("show");
		try {
			Iterable<Connection> connections = network.getConnections(
					getName(), spaceName);
			VectorSpace response = null;
			// TODO spawn threads
			for (Connection connection : connections) {
				use(connection);
				VectorSpace space = StatementBuilders.show(spaceName).execute(
						connection);
				if (response == null) {
					response = space;
				} else {
					response.insertAll(space);
				}
			}
			return response == null ? new SimpleVectorSpace(0) : response;
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	public Iterable<String> list() {
		log.info("list");
		try {
			return network.getSpaces(getName());
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
	}

	public String getName() {
		return databaseName;
	}

}
