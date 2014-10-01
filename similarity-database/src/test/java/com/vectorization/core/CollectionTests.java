package com.vectorization.core;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.vectorization.core.collection.SimpleVectorSpace;
import com.vectorization.core.collection.VPTVectorSpace;
import com.vectorization.core.collection.VectorSpace;
import com.vectorization.core.vectors.Vectors;

public class CollectionTests {

	@Test
	public void testSimpleVectorSpace() {
		VectorSpace expected = new SimpleVectorSpace(2, Arrays.asList(
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75)));
		int k = expected.size();
		Vector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		Iterable<Vector> result = expected.retrieveKnn(k, prototype);
		for (Vector o : result) {
			Assert.assertTrue(expected.contains(o));
		}
	}

	@Test
	public void testVPTCollection() {
		VectorSpace expected = new VPTVectorSpace(2,
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75));
		int k = expected.size();
		Vector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		Iterable<Vector> result = expected.retrieveKnn(k, prototype);
		for (Vector o : result) {
			Assert.assertTrue(expected.contains(o));
		}
	}
}
