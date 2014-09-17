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
package com.vectorization.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.collection.Space;
import com.vectorization.core.collection.VantagePointForest;

/**
 * Provides a Database object to interact with persisted collections of vectors.
 * Loads existing instances into memory or creates a new instance during
 * construction.
 * 
 * @author Robert Moss
 *
 */
public class Database {

	private final String databaseName;
	private Map<String, SSCollection<SSVector>> data = new LinkedHashMap<String, SSCollection<SSVector>>();

	private String currentTableName = "";
	private SSCollection<SSVector> currentTable;

	public Database(String databaseName) {
		this.databaseName = databaseName;
		try {
			loadAll();
		} catch (IOException e) {
			throw new SSException(e);
		}
	}

	private File getDatabaseDir() {
		File dir = new File(databaseName);
		if (!dir.exists())
			dir.mkdir();
		if (!dir.isDirectory())
			throw new SSException("No such database");
		return dir;
	}

	private File[] getDbFiles(File dir) {
		return dir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".db");
			}
		});
	}

	private File[] getDbFiles(File dir, FilenameFilter filter) {
		return dir.listFiles(filter);
	}

	private String removeSuffix(String filename) {
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	private void loadAll() throws IOException {
		try {
			File dir = getDatabaseDir();
			File[] tables = getDbFiles(dir);
			load(tables);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void addToPartList(
			Map<String, List<SSCollection<SSVector>>> partLists,
			SSCollection<SSVector> table, String tableName) {
		if (!partLists.containsKey(tableName)) {
			partLists.put(tableName, new ArrayList<SSCollection<SSVector>>());
		}
		List<SSCollection<SSVector>> parts = partLists.get(tableName);
		parts.add(table);
	}

	private void load(final String space) {
		File[] tables = getDbFiles(getDatabaseDir(), new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(space + ".") && name.endsWith(".db");
			}
		});
		try {
			load(tables);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void load(File[] tables) throws FileNotFoundException,
			ClassNotFoundException, IOException {
		Map<String, List<SSCollection<SSVector>>> partLists = new LinkedHashMap<String, List<SSCollection<SSVector>>>();
	
		for (File f : tables) {
			String filename = f.getName();
			SSCollection<SSVector> table = load(f);
			String tableName = removeSuffix(filename);
			if (!tableName.contains(".part")) {
				data.put(tableName, table);
			} else {
				tableName = removeSuffix(tableName);
				addToPartList(partLists, table, tableName);
			}
		}
	
		for (Entry<String, List<SSCollection<SSVector>>> parts : partLists
				.entrySet()) {
			int dimensionality = parts.getValue().get(0).dimensionality();
			data.put(parts.getKey(), new VantagePointForest<SSVector>(
					dimensionality, parts.getValue()));
		}
	}

	@SuppressWarnings("unchecked")
	private SSCollection<SSVector> load(File file)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			return (SSCollection<SSVector>) ois.readObject();
		} finally {
			if (ois != null)
				ois.close();
		}
	}

	public Database create(String tableName, int dimensionality) {
		assertNameNotNull(tableName, "create");
		SSCollection<SSVector> table = new VantagePointForest<SSVector>(
				dimensionality);
		data.put(tableName, table);
		currentTable = table;
		currentTableName = tableName;
		saveTable(tableName);
		return this;
	}

	public Database select(String string) {
		assertNameNotNull(string, "select");
		if (!data.containsKey(string))
			throw new SSException("No such table: " + string);
		load(string);
		currentTable = data.get(string);
		currentTableName = string;
		return this;
	}

	public void drop(String string) {
		assertNameNotNull(string, "drop");
		data.remove(string);
		deleteTableFile(string);
	} 

	private void deleteTableFile(final String string) {
		File dir = new File(databaseName);
		File[] tables = dir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(string + ".") && name.endsWith(".db");
			}
		});
		for (File f : tables) {
			f.delete();
		}
	}

	public String getCurrentTableName() {
		assertNameNotNull(currentTableName, "get current table name");
		return currentTableName;
	}

	private static void assertNameNotNull(String name, String operation) {
		if (name == null)
			throw new SSException("cannot " + operation
					+ ": table name must not be null");
	}

	public SSCollection<SSVector> retrieveKnn(int k, SSVector prototype) {
		if (currentTable == null)
			throw new SSException("No table selected");
		return retrieveKnn(k, prototype, currentTableName);
	}

	public SSCollection<SSVector> retrieveKnn(int k, SSVector prototype,
			String tableName) {
		load(tableName);
		if (!data.containsKey(tableName))
			throw new SSException("No such table: " + tableName);
		if (data.get(tableName).dimensionality() != prototype.dimensionality()) {
			throw new SSException(
					"Cannot search using element with dimensionality: "
							+ prototype.dimensionality()
							+ " in collection with dimensionality:"
							+ data.get(tableName).dimensionality());
		}
		return data.get(tableName).retrieveKnn(k, prototype);
	}
	
	public Database remove(String... myObject ){
		return removeAndSave(currentTableName, myObject);
	}

	public Database insert(SSVector... myObject) {
		return insertAndSave(currentTableName, myObject);
	}

	public Database removeAndSave(String table, String... myObjects){
		remove(table, myObjects);
		saveTable(table);
		return this;
	}
	
	public Database insertAndSave(String table, SSVector... myObjects) {
		insert(table, myObjects);
		saveTable(table);
		return this;
	}
	
	private void remove(String table, String... myObject) {
		if (!data.containsKey(table))
			throw new SSException("No such table: " + table);
		data.get(table).removeAll(myObject);
	}

	private void insert(String table, SSVector... myObject) {
		if (!data.containsKey(table))
			throw new SSException("No such table: " + table);
		data.get(table).insertAll(myObject);
	}

	private void saveTable(String table) {
		try {
			data.get(table).save(databaseName + File.separator + table);
		} catch (IOException e) {
			throw new SSException(e);
		}
	}

	public SSCollection<SSVector> show() {
		return show(getCurrentTableName());
	}

	public SSVector show(String tableName, String vectorName) {
		return show(tableName).get(vectorName);
	}

	public SSCollection<SSVector> show(String tableName) {
		load(tableName);
		if (!data.containsKey(tableName))
			return new Space<SSVector>(0);
		return data.get(tableName);
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
