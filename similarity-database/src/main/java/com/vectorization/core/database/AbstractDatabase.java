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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vectorization.core.Vector;
import com.vectorization.core.VectorizationException;
import com.vectorization.core.collection.FileCompositeCollection;
import com.vectorization.core.collection.SimpleVectorSpace;

public abstract class AbstractDatabase implements Database {

	private static final Logger log = LoggerFactory
			.getLogger(AbstractDatabase.class);

	private final String databaseName;
	private Map<String, FileCompositeCollection> data = new LinkedHashMap<String, FileCompositeCollection>();
	private SpaceFactory defaultSpaceFactory;
	private SpaceLoader spaceLoader = new SpaceLoader(this);
	private Properties props;

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

	private void saveProperties(File dir) throws FileNotFoundException,
			IOException {
		log.debug("saving properties" + props);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(dir, "db.properties"));
			props.store(pw, databaseName + " properties");
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	public Database create(String spaceName, int dimensionality) {
		return create(spaceName, dimensionality, getDefaultSpaceFactory());
	}

	public Database create(String spaceName, int dimensionality,
			SpaceFactory factory) {
		assertNameNotNull(spaceName, "create");
		FileCompositeCollection space = factory.createSpace(dimensionality,
				getName(), spaceName);
		insertSpace(spaceName, space);
		props.setProperty(spaceName + ".dimensionality",
				Integer.toString(dimensionality));
		try {
			saveProperties(getDatabaseDir());
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
		log.info(databaseName + "." + spaceName + " created");
		return this;
	}

	public void drop(String space) {
		assertNameNotNull(space, "drop");
		try {
			props.remove(space + ".dimensionality");
			saveProperties(getDatabaseDir());
			data.remove(space);
			deleteTableFile(space);
		} catch (Exception e) {
			throw new VectorizationException(e);
		}

		log.info(databaseName + "." + space + " dropped");
	}

	private void deleteTableFile(String space) {
		try {
			File dir = getDatabaseDir();
			File[] spaceFiles = dir.listFiles(SpaceLoader
					.createPartFilter(space));
			for (File f : spaceFiles) {
				f.delete();
				log.info(f.getName()+ " deleted");
			}
		} catch (IOException e) {
			throw new VectorizationException(e);
		}
	}

	public Iterable<Vector> retrieveKnn(String spaceName, int k,
			Vector prototype) {
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
		return this;
	}

	public Database insertAndSave(String space, Vector... vectors) {
		insert(space, vectors);
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

	public void insertSpace(String spaceName, FileCompositeCollection space) {
		data.put(spaceName, space);
	}

	public Vector show(String spaceName, String vectorName) {
		return data.get(spaceName).get(vectorName);
	}

	public Iterable<Vector> show(String spaceName) {
		// spaceLoader.load(data, spaceName, getDefaultSpaceFactory());
		if (!data.containsKey(spaceName)) {
			return new SimpleVectorSpace(0);
		}
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

	public void setProperties(Properties dbProperties) {
		props = dbProperties;
	}
}