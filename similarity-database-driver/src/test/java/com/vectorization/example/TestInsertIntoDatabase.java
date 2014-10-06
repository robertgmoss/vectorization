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
package com.vectorization.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.vectorization.core.Vector;
import com.vectorization.core.collection.VectorSpace;
import com.vectorization.core.vectors.Vectors;
import com.vectorization.driver.VectorizationConnection;
import com.vectorization.driver.builders.StatementBuilders;

public class TestInsertIntoDatabase {

	private VectorizationConnection connection;
	private String spaceName = "insertTestSpace";

	@Before
	public void setUp() {
		App app = new App();
		connection = app.getConnection();
		connection.connect();
		StatementBuilders.login("admin").with("admin").execute(connection);
		StatementBuilders.use("Test").execute(connection);
		StatementBuilders.create(spaceName).withDimensionality(2)
				.execute(connection);
	}

	@After
	public void tearDown() {
		String result = StatementBuilders.drop(spaceName).execute(connection);
		System.out.println(result);
		connection.close();
	}

	//@Test
	public void testInsert() {
		String id = "a";
		Vector vector = Vectors.createNormalisedVector(id, Math.random(), Math.random());
		String result = StatementBuilders
				.insert(vector)
				.into(spaceName)
				.execute(connection);
		System.out.println(result);
		VectorSpace space = StatementBuilders.show(spaceName, id).execute(connection);
		Assert.assertEquals(1, space.size());
		Assert.assertTrue(space.contains(vector));
		Assert.assertEquals(vector, space.get(id));
		
	}
}
