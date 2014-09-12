package com.vectorization.core;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vectorization.core.collection.SSCollection;
import com.vectorization.core.collection.Set;

public class CRUDTest {

	private Database database;

	@Before
	public void setUp() {
		database = new Database("Test");
		database.create("test", 2);
		database.insert(Vectors.createNormalisedVector("1", 0.8, 0.5))
				.insert(Vectors.createNormalisedVector("2", 0.25, 0.75));
	}

	@After
	public void tearDown() {
		// database.drop("test");
		// database.drop("mydata");
	}


	@Test(expected = SSException.class)
	public void testCreate() {
		String table = "mydata";
		database.drop(table);
		database.select(table);
		Assert.assertFalse(table.equals(database.getCurrentTableName()));
		database.create(table, 2);
		Assert.assertTrue(table.equals(database.getCurrentTableName()));

		database.create(null, 0); // throws exception
	}

	@Test
	public void testRetrieve() {
		SSCollection<SSVector> expected = new Set<SSVector>(2, 
				Arrays.asList(Vectors.createNormalisedVector("1", 0.8, 0.5), Vectors.createNormalisedVector("2", 0.25, 0.75)));
		int k = expected.size();
		SSVector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		SSCollection<? extends SSVector> result = database.select("test").retrieveKnn(k, prototype);
		for (SSVector o : expected) {
			Assert.assertTrue(result.contains(o));
		}
		System.out.println(result);
	}
}
