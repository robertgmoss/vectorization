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
package com.vectorization.core.database;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;
import com.vectorization.core.collection.SimpleVectorSpace;
import com.vectorization.core.collection.VectorCollection;

public abstract class AbstractDatabase implements Database {

	private final String databaseName;
	private Map<String, VectorCollection> data = new LinkedHashMap<String, VectorCollection>();
	private SpaceFactory defaultSpaceFactory;
	private SpaceLoader spaceLoader = new SpaceLoader(this);

	private static void assertNameNotNull(String name, String operation) {
		if (name == null)
			throw new VectorizationException("cannot " + operation
					+ ": table name must not be null");
	}

	public AbstractDatabase(String databaseName) {
		this(databaseName, new VantagePointForestSpaceFactory());
	}

	public AbstractDatabase(String databaseName, SpaceFactory factory) {
		this.databaseName = databaseName;
		this.defaultSpaceFactory = factory;
		spaceLoader.loadAll(data, factory);
	}



	public File getDatabaseDir() throws IOException {
		File dir = new File(databaseName);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!dir.isDirectory())
			throw new VectorizationException("No such database");
		return dir;
	}

	public Database create(String spaceName, int dimensionality) {
		return create(spaceName, dimensionality, getDefaultSpaceFactory());
	}

	public Database create(String spaceName, int dimensionality,
			SpaceFactory factory) {
		assertNameNotNull(spaceName, "create");
		VectorCollection space = factory.createSpace(dimensionality);
		insertSpace(spaceName, space);
		saveSpace(spaceName);
		return this;
	}

	public void drop(String space) {
		assertNameNotNull(space, "drop");
		data.remove(space);
		deleteTableFile(space);
	}

	private void deleteTableFile(String space) {
		try {
			File dir = getDatabaseDir();
			File[] spaceFiles = dir.listFiles(SpaceLoader
					.createPartFilter(space));
			for (File f : spaceFiles) {
				f.delete();
			}
		} catch (IOException e) {
			throw new VectorizationException(e);
		}
	}

	public VectorCollection retrieveKnn(String spaceName, int k,
			Vector prototype) {
		long before = System.nanoTime();
		spaceLoader.load(data, spaceName, getDefaultSpaceFactory());
		System.out.println("loaded space [" + (System.nanoTime() - before) + " ns]");
		if (!data.containsKey(spaceName))
			throw new VectorizationException("No such table: " + spaceName);
		if (data.get(spaceName).dimensionality() != prototype.dimensionality()) {
			throw new VectorizationException(
					"Cannot search using element with dimensionality: "
							+ prototype.dimensionality()
							+ " in collection with dimensionality:"
							+ data.get(spaceName).dimensionality());
		}
		return data.get(spaceName).retrieveKnn(k, prototype);
	}

	private SpaceFactory getDefaultSpaceFactory() {
		return defaultSpaceFactory;
	}

	public Database removeAndSave(String space, String... vectorIds) {
		remove(space, vectorIds);
		saveSpace(space);
		return this;
	}

	public Database insertAndSave(String space, Vector... vectors) {
		insert(space, vectors);
		saveSpace(space);
		return this;
	}

	private void remove(String space, String... vectorIds) {
		if (!data.containsKey(space))
			throw new VectorizationException("No such table: " + space);
		data.get(space).removeAll(vectorIds);
	}

	private void insert(String space, Vector... vectors) {
		if (!data.containsKey(space))
			throw new VectorizationException("No such table: " + space);
		data.get(space).insertAll(vectors);
	}

	public void insertSpace(String spaceName, VectorCollection space) {
		long before = System.nanoTime();
		data.put(spaceName, space);
		System.out.println("inserted space [" + (System.nanoTime() - before) + " ns]");
	}

	private void saveSpace(String space) {
		try {
			//System.out.print("saving...");
			data.get(space).save(databaseName + File.separator + space);
			//System.out.println("done");
		} catch (IOException e) {
			throw new VectorizationException(e);
		}
	}

	public Vector show(String spaceName, String vectorName) {
		return show(spaceName).get(vectorName);
	}

	public VectorCollection show(String spaceName) {
		spaceLoader.load(data, spaceName, getDefaultSpaceFactory());
		if (!data.containsKey(spaceName))
			return new SimpleVectorSpace(0);
		return data.get(spaceName);
	}

	public Iterable<String> list() {
		return data.keySet();
	}
	
	public String getName() {
		return databaseName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append(databaseName);
		sb.append("\"");
		return sb.toString();
	}
}