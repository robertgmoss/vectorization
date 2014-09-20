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

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.collection.Space;
import com.vectorization.core.database.Database;
import com.vectorization.core.database.DatabaseImpl;

public class CRUDTest {

	private Database database;

	@Before
	public void setUp() {
		database = new DatabaseImpl("Test");
		database.drop("large");
		database.create("test", 2);
		database.insertAndSave("test",
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75));
	}

	@After
	public void tearDown() {
		database.drop("test");
		database.drop("mydata");
		database.drop("large");
	}

	@Test
	public void testRetrieve() {
		SSCollection<SSVector> expected = new Space<SSVector>(2, Arrays.asList(
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75)));
		int k = expected.size();
		SSVector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		SSCollection<? extends SSVector> result = database.retrieveKnn("test", k,
				prototype);
		for (SSVector o : expected) {
			Assert.assertTrue(result.contains(o));
		}
		System.out.println(result);
	}

	// @Test
	public void testRetrieveFromLargeSpace() {
		database.create("large", 2);
		SSVector[] vs = new SSVector[1000000];
		for (int i = 0; i < vs.length; i++) {
			vs[i] = Vectors.createNormalisedVector("" + i, Math.random(),
					Math.random());
		}
		database.insertAndSave("large", vs);
		System.out.println("searching...");
		System.out.println(database.retrieveKnn(
				"large",
				1,
				Vectors.createNormalisedVector("q", Math.random(), Math.random())));
		System.out.println();
	}
}
