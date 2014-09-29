package com.vectorization.core;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.vectorization.core.collection.SimpleVectorSpace;
import com.vectorization.core.collection.VPFCollection;
import com.vectorization.core.collection.VPTCollection;
import com.vectorization.core.collection.VectorCollection;
import com.vectorization.core.vectors.Vectors;

public class CollectionTests {

	@Test
	public void testSimpleVectorSpace(){
		VectorCollection expected = new SimpleVectorSpace(2, Arrays.asList(
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75)));
		int k = expected.size();
		Vector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		VectorCollection result = expected.retrieveKnn(k,
				prototype);
		for (Vector o : expected) {
			Assert.assertTrue(result.contains(o));
		}
	}
	
	@Test
	public void testVPTCollection(){
		VectorCollection expected = new VPTCollection(2,
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75));
		int k = expected.size();
		Vector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		VectorCollection result = expected.retrieveKnn(k,
				prototype);
		for (Vector o : expected) {
			Assert.assertTrue(result.contains(o));
		}
	}
	
	@Test
	public void testVPFCollection(){
		VectorCollection tree = new VPTCollection(2,
				Vectors.createNormalisedVector("1", 0.8, 0.5),
				Vectors.createNormalisedVector("2", 0.25, 0.75));
		VPFCollection forest = new VPFCollection(2, Arrays.asList(tree));
		int k = tree.size();
		Vector prototype = Vectors.createNormalisedVector("", 1.0, 0.0);
		
		VectorCollection result = forest.retrieveKnn(k,
				prototype);
		for (Vector o : tree) {
			Assert.assertTrue(result.contains(o));
		}
	}
}
