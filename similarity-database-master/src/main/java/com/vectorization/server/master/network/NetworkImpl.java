package com.vectorization.server.master.network;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vectorization.core.VectorizationException;
import com.vectorization.driver.VectorizationConnection;
import com.vectorization.driver.builders.StatementBuilders;
import com.vectorization.server.master.MasterConnection;

@Singleton
public class NetworkImpl implements Network {

	private static final String CREATE_SERVERS = "create table servers ("
			+ "    name varchar(255) primary key,"
			+ "    host varchar(255) not null,"
			+ "    port smallint default 4567" + ")";

	private static final String CREATE_DATABASES = "create table databases ("
			+ "    name varchar(255) primary key" + ")";

	private static final String CREATE_DATABASE_SPACES = "create table database_spaces ("
			+ "    dbname varchar(255) not null,"
			+ "    servername varchar(255) not null,"
			+ "    spacename varchar(255) not null,"
			+ "    primary key(dbname, servername, spacename)" + ")";

	private static final String INSERT_SERVER = "insert into servers values (?, ?, ?)";
	private static final String INSERT_DATABASE = "insert into databases values (?)";
	private static final String INSERT_DATABASE_SPACE = "insert into database_spaces values (?, ?, ?)";

	private static final String SELECT_SERVERS = "select name, host, port from servers";
	private static final String SELECT_SPACES = "select distinct spacename "
			+ "    from database_spaces " + "    where dbname = ?";
	private static final String SELECT_SERVERS_FOR_SPACE = "select servername "
			+ "    from database_spaces " + "    where dbname = ? "
			+ "    and spacename = ?";

	private static final Logger log = LoggerFactory
			.getLogger(NetworkImpl.class);

	private static final String DROP_SPACE = "delete from database_spaces"
			+ "    where dbname = ? " + "    and spacename = ?";

	private final Map<String, VectorizationConnection> connections = new ConcurrentHashMap<String, VectorizationConnection>();
	private final DataSource dataSource;

	@Inject
	public NetworkImpl(DataSource dataSource) {
		this.dataSource = dataSource;
		initDatabaseLoggingException();
		initServerConnections();
	}

	private void initServerConnections() {
		try {
			initServerConnectionsLoggingException();
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
	}

	private void initServerConnectionsLoggingException() throws SQLException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(SELECT_SERVERS);
			while (resultSet.next()) {
				final String name = resultSet.getString("name");
				final String host = resultSet.getString("host");
				final int port = resultSet.getInt("port");
				Runnable r = new Runnable() {

					public void run() {
						createConnection(name, host, port);
					}
				};

				new Thread(r).start();

			}
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (conn != null)
				conn.close();
		}
	}

	private void createConnection(String name, String host, int port) {
		try {
			MasterConnection c = new MasterConnection(host, port, this);
			c.connect();
			StatementBuilders.login("admin").with("admin").execute(c);
			connections.put(name, c);
		} catch (Exception e) {
			log.error("could not connect to " + name + ": " + e.getMessage());
		}
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
			statement.execute(CREATE_SERVERS);
			statement.execute(CREATE_DATABASES);
			statement.execute(CREATE_DATABASE_SPACES);
			insertServer("localhost", "localhost", 4567);
		} finally {
			if (statement != null)
				statement.close();
			if (conn != null)
				conn.close();
		}
	}

	public void insertServer(final String id, final String location, final int port)
			throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertServer(conn, id, location, port);
		} catch (SQLException e) {
			throw new VectorizationException("can't insert server: " + e);
		} finally {
			if (conn != null)
				conn.close();
		}
		new Thread() {
			@Override
			public void run() {
				createConnection(id, location, port);
			};
		}.start();
	}

	private void insertServer(Connection conn, String id, String location,
			int port) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_SERVER);
			ps.setString(1, id);
			ps.setString(2, location);
			ps.setInt(3, port);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public void insertDatabase(String databaseName) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertDatabase(conn, databaseName);
		} catch (SQLException e) {
			throw new VectorizationException("can't insert database: " + e);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private void insertDatabase(Connection conn, String databaseName)
			throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_DATABASE);
			ps.setString(1, databaseName);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public void insertDatabaseSpace(String databaseName, String serverId,
			String spaceName) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			insertDatabaseSpace(conn, databaseName, serverId, spaceName);
		} catch (SQLException e) {
			throw new VectorizationException("can't insert database: " + e);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private void insertDatabaseSpace(Connection conn, String databaseName,
			String serverId, String spaceName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_DATABASE_SPACE);
			ps.setString(1, databaseName);
			ps.setString(2, serverId);
			ps.setString(3, spaceName);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public VectorizationConnection getConnection(String serverId) {
		if (!connections.containsKey(serverId))
			throw new VectorizationException("no such server:" + serverId);
		return connections.get(serverId);
	}

	public Iterable<String> getSpaces(String name) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			return getSpaces(conn, name);
		} catch (SQLException e) {
			throw new VectorizationException("can't insert database: " + e);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private Iterable<String> getSpaces(Connection conn, String name)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement(SELECT_SPACES);
			ps.setString(1, name);
			resultSet = ps.executeQuery();
			List<String> spaces = new ArrayList<String>();
			while (resultSet.next()) {
				String space = resultSet.getString("spacename");
				spaces.add(space);
			}
			return spaces;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public Iterable<VectorizationConnection> getConnections(
			String databaseName, String space) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			return getConnections(conn, databaseName, space);
		} catch (SQLException e) {
			throw new VectorizationException("can't get Connections: " + e);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private List<VectorizationConnection> getConnections(Connection conn,
			String databaseName, String space) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement(SELECT_SERVERS_FOR_SPACE);
			ps.setString(1, databaseName);
			ps.setString(2, space);
			resultSet = ps.executeQuery();
			List<VectorizationConnection> connections = new ArrayList<VectorizationConnection>();
			while (resultSet.next()) {
				String servername = resultSet.getString("servername");
				connections.add(this.connections.get(servername));
			}
			return connections;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	public String getRandomServer() {
		List<String> serverIds = new ArrayList<String>(connections.keySet());
		return serverIds.get((int) (Math.random() * serverIds.size()));
	}

	public void drop(String databaseName, String space) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			drop(conn, databaseName, space);
		} catch (SQLException e) {
			throw new VectorizationException("can't get Connections: " + e);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private void drop(Connection conn, String databaseName, String space)
			throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(DROP_SPACE);
			ps.setString(1, databaseName);
			ps.setString(2, space);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}
}
