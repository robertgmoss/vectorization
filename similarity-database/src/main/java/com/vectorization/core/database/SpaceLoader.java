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

import com.vectorization.core.SSException;
import com.vectorization.core.SSVector;
import com.vectorization.core.collection.SSCollection;

public class SpaceLoader {

	private AbstractDatabase database;

	public SpaceLoader(AbstractDatabase database) {
		this.database = database;
	}
	
	public static File[] getDbFiles(File dir) {
		return getDbFiles(dir, new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".db");
			}
		});
	}

	public static File[] getDbFiles(File dir, FilenameFilter filter) {
		return dir.listFiles(filter);
	}

	public void loadAll(Map<String, SSCollection<SSVector>> data, SpaceFactory factory) {
		try {
			File[] tables = getDbFiles(database.getDatabaseDir());
			load(data, tables, factory);
		} catch (Exception e) {
			throw new SSException(e);
		}
	}

	public void load(Map<String, SSCollection<SSVector>> data, String space,
			SpaceFactory factory) {

		File[] tables = getDbFiles(database.getDatabaseDir(),
				createPartFilter(space));
		try {
			load(data, tables, factory);
		} catch (Exception e) {
			throw new SSException(e);
		}
	}

	public static FilenameFilter createPartFilter(final String space) {
		return new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(space + ".") && name.endsWith(".db");
			}
		};
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

	private void load(Map<String, SSCollection<SSVector>> data, File[] tables,
			SpaceFactory factory) throws FileNotFoundException,
			ClassNotFoundException, IOException {
		Map<String, List<SSCollection<SSVector>>> partLists = new LinkedHashMap<String, List<SSCollection<SSVector>>>();

		for (File f : tables) {
			String filename = f.getName();
			SSCollection<SSVector> table = load(f);
			String tableName = removeSuffix(filename);
			if (!tableName.contains(".part")) {
				database.insertSpace(tableName, table);
			} else {
				tableName = removeSuffix(tableName);
				addToPartList(partLists, table, tableName);
			}
		}
		flattenPartLists(data, partLists, factory);

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

	private String removeSuffix(String filename) {
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	private void flattenPartLists(Map<String, SSCollection<SSVector>> data,
			Map<String, List<SSCollection<SSVector>>> partLists,
			SpaceFactory factory) {
		for (Entry<String, List<SSCollection<SSVector>>> parts : partLists
				.entrySet()) {
			int dimensionality = parts.getValue().get(0).dimensionality();
			database.insertSpace(
					parts.getKey(),
					factory.createCompositeTable(dimensionality,
							parts.getValue()));
		}
	}

}
