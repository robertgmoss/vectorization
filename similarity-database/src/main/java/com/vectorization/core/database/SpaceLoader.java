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
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.vectorization.core.VectorizationException;
import com.vectorization.core.collection.FileCompositeCollection;

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

	public static Properties getDbProperties(File dir) throws IOException {
		Properties props = new Properties();
		props.load(new FileReader(new File(dir, "db.properties")));
		return props;
	}

	public static File[] getDbFiles(File dir, FilenameFilter filter) {
		return dir.listFiles(filter);
	}

	public void loadAll(Map<String, FileCompositeCollection> data, SpaceFactory factory) {
		try {
			File databaseDir = database.getDatabaseDir();
			//Properties props = getDbProperties(databaseDir);
			//String spaces = props.getProperty("spaces");
			//if (spaces != null) {
				Set<String> spaceNames = getSpaceNames(databaseDir);
				for (String space : spaceNames) {
					System.out.println("loading space: " + space);
					load(data, space, factory);
				}
			//}
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
	}

	private Set<String> getSpaceNames(File databaseDir) {
		String[] files = databaseDir.list(new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				return name.endsWith(".db");
			}
		});
		
		Set<String> result = new LinkedHashSet<String>();
		for(String f : files){
			result.add(f.substring(0, f.indexOf(".")));
		}
		return result;
	}

	public void load(Map<String, FileCompositeCollection> data, String space,
			SpaceFactory factory) {
		try {
			File databaseDir = database.getDatabaseDir();
			File[] tables = getDbFiles(databaseDir,
					createPartFilter(space));
			
			if (tables.length > 0) {
				Properties props = getDbProperties(databaseDir);
				int dim = Integer.parseInt(props.getProperty(space+".dimensionality"));
				FileCompositeCollection composite = factory.createSpace(dim,database.getName(), space,
						tables);
				data.put(space, composite);
			}
		} catch (Exception e) {
			throw new VectorizationException(e);
		}
	}

	public static FilenameFilter createPartFilter(final String space) {
		return new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(space + ".part") && name.endsWith(".db");
			}
		};
	}
}
