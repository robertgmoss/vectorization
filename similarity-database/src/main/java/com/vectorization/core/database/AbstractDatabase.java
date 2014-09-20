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

import com.vectorization.core.SSException;
import com.vectorization.core.SSVector;
import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.collection.Space;

public abstract class AbstractDatabase implements Database {

	private final String databaseName;
	private Map<String, SSCollection<SSVector>> data = new LinkedHashMap<String, SSCollection<SSVector>>();
	private SpaceFactory defaultSpaceFactory;
	private SpaceLoader spaceLoader = new SpaceLoader(this);

	private static void assertNameNotNull(String name, String operation) {
		if (name == null)
			throw new SSException("cannot " + operation
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

	public File getDatabaseDir() {
		File dir = new File(databaseName);
		if (!dir.exists())
			dir.mkdir();
		if (!dir.isDirectory())
			throw new SSException("No such database");
		return dir;
	}

	public Database create(String spaceName, int dimensionality) {
		return create(spaceName, dimensionality, getDefaultSpaceFactory());
	}

	public Database create(String spaceName, int dimensionality,
			SpaceFactory factory) {
		assertNameNotNull(spaceName, "create");
		SSCollection<SSVector> space = factory.createSpace(dimensionality);
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
		File dir = getDatabaseDir();
		File[] spaceFiles = dir.listFiles(SpaceLoader.createPartFilter(space));
		for (File f : spaceFiles) {
			f.delete();
		}
	}

	public SSCollection<SSVector> retrieveKnn(String spaceName, int k,
			SSVector prototype) {
		spaceLoader.load(data, spaceName, getDefaultSpaceFactory());
		if (!data.containsKey(spaceName))
			throw new SSException("No such table: " + spaceName);
		if (data.get(spaceName).dimensionality() != prototype.dimensionality()) {
			throw new SSException(
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

	public Database insertAndSave(String space, SSVector... vectors) {
		insert(space, vectors);
		saveSpace(space);
		return this;
	}

	private void remove(String space, String... vectorIds) {
		if (!data.containsKey(space))
			throw new SSException("No such table: " + space);
		data.get(space).removeAll(vectorIds);
	}

	private void insert(String space, SSVector... vectors) {
		if (!data.containsKey(space))
			throw new SSException("No such table: " + space);
		data.get(space).insertAll(vectors);
	}

	public void insertSpace(String spaceName, SSCollection<SSVector> space) {
		data.put(spaceName, space);
	}

	private void saveSpace(String space) {
		try {
			data.get(space).save(databaseName + File.separator + space);
		} catch (IOException e) {
			throw new SSException(e);
		}
	}

	public SSVector show(String spaceName, String vectorName) {
		return show(spaceName).get(vectorName);
	}

	public SSCollection<SSVector> show(String spaceName) {
		spaceLoader.load(data, spaceName, getDefaultSpaceFactory());
		if (!data.containsKey(spaceName))
			return new Space<SSVector>(0);
		return data.get(spaceName);
	}

	public Iterable<String> list() {
		return data.keySet();
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