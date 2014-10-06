package com.vectorization.server.master;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;
import com.vectorization.core.collection.SimpleVectorSpace;
import com.vectorization.core.collection.VectorSpace;
import com.vectorization.core.database.Database;
import com.vectorization.driver.VectorizationConnection;
import com.vectorization.driver.builders.StatementBuilders;
import com.vectorization.server.master.network.Network;

public class DistributedDatabase implements Database {

	private static final Logger log = LoggerFactory.getLogger(DistributedDatabase.class);
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final String databaseName;
	private final Network network;

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

	private void use(VectorizationConnection connection) {
		String result = StatementBuilders.use(getName()).execute(connection);
		log.info(result);
	}
	
	private <E> CompletionService<E> newCompletionService(){
		return new ExecutorCompletionService<E>(executor);
	}

	public Database create(String spaceName, int dimensionality) {
		log.info("create");
		String serverId = network.getRandomServer();
		try {
			network.insertDatabaseSpace(databaseName, serverId, spaceName);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
		VectorizationConnection connection = network.getConnection(serverId);
		use(connection);
		String result = StatementBuilders.create(spaceName)
				.withDimensionality(dimensionality).execute(connection);
		log.info(result);
		return this;
	}

	public void drop(final String space) {
		log.info("drop");
		try {

			CompletionService<String> completionService = newCompletionService();
			Iterable<VectorizationConnection> connections = network.getConnections(getName(), space);
			for (final VectorizationConnection connection : connections) {
				completionService.submit(new Callable<String>() {

					public String call() throws Exception {
						use(connection);
						return StatementBuilders.drop(space)
								.execute(connection);
					}

				});
			}

			for (VectorizationConnection connection : connections) {
				Future<String> future = completionService.take();
				log.info(future.get());
			}
			network.drop(getName(), space);
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
	}

	public Iterable<Vector> retrieveKnn(final String spaceName, final int k,
			final Vector prototype) {
		log.info("retrieveKnn");
		try {
			CompletionService<VectorSpace> completionService = newCompletionService();
			VectorSpace result = null;
			Iterable<VectorizationConnection> connections = network.getConnections(getName(), spaceName);
			for (final VectorizationConnection connection : connections) {
				completionService.submit(new Callable<VectorSpace>() {

					public VectorSpace call() throws Exception {
						use(connection);
						return StatementBuilders.find(k).nearestTo(prototype)
								.in(spaceName).execute(connection);
					}

				});
			}
			
			for (final VectorizationConnection connection : connections) {
				Future<VectorSpace> future = completionService.take();
				VectorSpace found = future.get();
				if (result == null) {
					result = found;
				} else {
					result.insertAll(found);
				}
			}
			return result == null ? new SimpleVectorSpace(0) : result;
		} catch (Exception e) {
			throw new VectorizationException(e);
		}

	}

	public Database remove(final String space, final String... vectors) {
		log.info("remove");
		try {
			CompletionService<String> completionService = newCompletionService();
			Iterable<VectorizationConnection> connections = network.getConnections(getName(), space);
			for (final VectorizationConnection connection : connections) {
				completionService.submit(new Callable<String>(){

					public String call() throws Exception {
						use(connection);
						return StatementBuilders.remove(vectors).from(space)
								.execute(connection);
					}
					
				});
			}
			
			for(VectorizationConnection connection : connections){
				Future<String> future = completionService.take();
				log.info(future.get());
			}
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
		return this;
	}

	public Database insert(final String space, final Vector... vectors) {
		log.info("insert");
		try {
			CompletionService<String> completionService = newCompletionService();
			Iterable<VectorizationConnection> connections = network.getConnections(getName(), space);
			int count = 0;
			for (final VectorizationConnection connection : connections) {
				count++;
			}
			List<Vector>[] chuncks = new List[count];
			for(int i = 0; i < vectors.length; i++){
				if(chuncks[i % count] == null) chuncks[i % count] = new ArrayList<Vector>();
				chuncks[i % count].add(vectors[i]);
			}
			
			int i = 0;
			for (final VectorizationConnection connection : connections) {
				final Vector[] v = chuncks[i].toArray(new Vector[chuncks[i].size()]);
				completionService.submit(new Callable<String>() {

					public String call() throws Exception {
						use(connection);
						return StatementBuilders.insert(v).into(space).execute(connection);
					}
				});
				i++;
			}
			
			// TODO keep count
			for(VectorizationConnection connection : connections){
				Future<String> future = completionService.take();
				log.info(future.get());
			}
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
		return this;
	}

	public Vector show(final String spaceName, final String vectorName) {
		log.info("show");
		try {
			CompletionService<VectorSpace> completionService = newCompletionService();
			Iterable<VectorizationConnection> connections = network.getConnections(getName(), spaceName);		
			for (final VectorizationConnection connection : connections) {
				completionService.submit(new Callable<VectorSpace>(){

					public VectorSpace call() throws Exception {
						use(connection);
						return StatementBuilders.show(spaceName, vectorName).execute(connection);
					}
					
				});
			}
			
			for(VectorizationConnection connection : connections){
				Future<VectorSpace> future = completionService.take();
				VectorSpace s = future.get();
				if (s.contains(vectorName)){
					return s.get(vectorName);
				}
			}
			throw new VectorizationException("No such vector");
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
	}

	public Iterable<Vector> show(final String spaceName) {
		log.info("show");
		try {
			CompletionService<VectorSpace> completionService = newCompletionService();
			Iterable<VectorizationConnection> connections = network.getConnections(getName(), spaceName);
			VectorSpace response = null;
			for (final VectorizationConnection connection : connections) {
				completionService.submit(new Callable<VectorSpace>() {

					public VectorSpace call() throws Exception {
						use(connection);
						return StatementBuilders.show(spaceName).execute(connection);
					}
				});
			}
			for (final VectorizationConnection connection : connections) {
				Future<VectorSpace> future = completionService.take();
				VectorSpace s = future.get();
				if (response == null) {
					response = s;
				} else {
					response.insertAll(s);
				}
			}
			return response == null ? new SimpleVectorSpace(0) : response;
		} catch (Exception e) {
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
