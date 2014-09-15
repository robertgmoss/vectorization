package com.vectorization.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.collection.Set;

/**
 * Provides a Database object to interact with persisted collections of vectors.  Loads existing
 * instances into memory or creates a new instance during construction.  
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

	private void loadAll() throws IOException {
		ObjectInputStream ois = null;
		try {
			File dir = new File(databaseName);
			if (!dir.exists()) dir.mkdir();
			if (!dir.isDirectory()) throw new SSException("No such database");
			File[] tables = dir.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					return name.endsWith(".db");
				}
			});
			for (File f : tables) {
				ois = new ObjectInputStream(new FileInputStream(f));
				SSCollection<SSVector> table = (SSCollection<SSVector>) ois.readObject();
				String filename = f.getName();
				String tableName = filename.substring(0, filename.lastIndexOf('.'));
				data.put(tableName, table);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) ois.close();
		}
	}

	public Database create(String tableName, int dimensionality) {
		assertNameNotNull(tableName, "create");
		SSCollection<SSVector> table = new Set<SSVector>(dimensionality);
		data.put(tableName, table);
		currentTable = table;
		currentTableName = tableName;
		saveTable(tableName);
		return this;
	}

	public Database select(String string) {
		assertNameNotNull(string, "select");
		if (!data.containsKey(string)) throw new SSException("No such table: " + string);
		currentTable = data.get(string);
		currentTableName = string;
		return this;
	}

	public void drop(String string) {
		assertNameNotNull(string, "drop");
		data.remove(string);
		deleteTableFile(string);
	}

	private String tableFileName(String string) {
		return databaseName + File.separator + string + ".db";
	}

	private void deleteTableFile(String string) {
		File f = new File(tableFileName(string));
		f.delete();
	}

	public String getCurrentTableName() {
		assertNameNotNull(currentTableName, "get current table name");
		return currentTableName;
	}

	private static void assertNameNotNull(String name, String operation) {
		if (name == null) throw new SSException("cannot " + operation
				+ ": table name must not be null");
	}

	public SSCollection<SSVector> retrieveKnn(int k, SSVector prototype) {
		if (currentTable == null) throw new SSException("No table selected");
		return retrieveKnn(k, prototype, currentTableName);
	}

	public SSCollection<SSVector> retrieveKnn(int k, SSVector prototype,
			String tableName) {
		if (!data.containsKey(tableName)) throw new SSException("No such table: " + tableName);
		if (data.get(tableName).dimensionality() != prototype.dimensionality()) { throw new SSException(
				"Cannot search using element with dimensionality: "
						+ prototype.dimensionality()
						+ " in collection with dimensionality:"
						+ data.get(tableName).dimensionality()); }
		return data.get(tableName).retrieveKnn(k, prototype);
	}

	public Database insert(SSVector myObject) {
		return insert(myObject, currentTableName);
	}

	public Database insert(SSVector myObject, String table) {
		if (!data.containsKey(table)) throw new SSException("No such table: " + table);
		data.get(table).insert(myObject);
		saveTable(table);
		return this;
	}

	private void saveTable(String table) {
		try {
			save(data.get(table), table);
		} catch (IOException e) {
			throw new SSException(e);
		}
	}

	private void save(SSCollection<SSVector> table,
			String tableName) throws IOException {
		ObjectOutputStream dos = null;
		try {
			dos = new ObjectOutputStream(new FileOutputStream(tableFileName(tableName)));
			dos.writeObject(table);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (dos != null) dos.close();
		}
	}

	public SSCollection<SSVector> show() {
		return show(getCurrentTableName());
	}

	public SSCollection<SSVector> show(String tableName) {
		if (!data.containsKey(tableName)) return new Set<SSVector>(0);
		return data.get(tableName);
	}
	
	public SSVector show(String tableName, String vectorName){
		if (!data.containsKey(tableName)) throw new SSException("No such space " + tableName);
		return data.get(tableName).get(vectorName);
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
