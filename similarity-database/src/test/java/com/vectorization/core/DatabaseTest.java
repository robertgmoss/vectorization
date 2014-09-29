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

import com.vectorization.core.collection.SimpleVectorSpace;
import com.vectorization.core.collection.VectorCollection;
import com.vectorization.core.database.Database;
import com.vectorization.core.database.DatabaseImpl;
import com.vectorization.core.vectors.Vectors;

public class DatabaseTest {

	private Database database;

	@Before
	public void setUp() {
		database = new DatabaseImpl("Test");
//		database.drop("large");
//		database.create("test", 2);
//		database.insertAndSave("test",
//				Vectors.createNormalisedVector("1", 0.8, 0.5),
//				Vectors.createNormalisedVector("2", 0.25, 0.75));
	}

	@After
	public void tearDown() {
		database.drop("test");
		database.drop("mydata");
		database.drop("large");
	}

	//@Test
	public void testRetrieve() {
		VectorCollection expected = new SimpleVectorSpace(2, Arrays.asList(
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75)));
		int k = expected.size();
		Vector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		VectorCollection result = database.retrieveKnn("test", k,
				prototype);
		for (Vector o : expected) {
			Assert.assertTrue(result.contains(o));
		}
	}

	@Test
	public void testRetrieveFromLargeSpace() {
		database.create("large", 2);
		Vector[] vs = new Vector[320000];
		for (int i = 0; i < vs.length; i++) {
			vs[i] = Vectors.createNormalisedVector("" + i, Math.random(),
					Math.random());
		}
		database.insertAndSave("large", vs);
		Vector q = Vectors.createNormalisedVector("q", Math.random(), Math.random());
		System.out.println("searching for neighbours of " + q);
		System.out.println(database.retrieveKnn(
				"large",
				5,
				q));
//		System.out.println();
//		System.out.println("showing whole space");
//		System.out.println(SimpleVectorSpace.parseSpace(database.show("large").toString()));
	}
}
