package com.vectorization.core.collection;

import java.util.ArrayList;
import java.util.List;

import com.vectorization.core.Vector;

public class VPTCollection extends AbstractVectorCollection {

	private static final long serialVersionUID = 7778317446717284965L;
	private VantagePointTree<Vector> vpt;

	public VPTCollection(int dimensionality, Vector... points) {
		super(dimensionality);
		insertAll(points);
	}

	public VectorCollection retrieveKnn(int k, Vector prototype) {
		SimpleVectorSpace sp = new SimpleVectorSpace(dimensionality());
		long before = System.nanoTime();
		VantagePointTree<Vector> vantagePointTree = vpt.retrieveKnn(k, prototype);
		System.out.println("vtp searched. [" + (System.nanoTime() - before) + " ns]");
		
		before = System.nanoTime();
		List<Vector> values = vantagePointTree.values();
		System.out.println("got values. [" + (System.nanoTime() - before) + " ns]");
		
		before = System.nanoTime();
		sp.insertAll(values);
		System.out.println("inserted values. [" + (System.nanoTime() - before) + " ns]");
		return sp.retrieveKnn(k, prototype);
	}
	
	@Override
	public VectorCollection removeAll(String... vectorNames) {
		super.removeAll(vectorNames);
		vpt = new VantagePointTree<Vector>(super.values());
		return this;
	}
	
	@Override
	public VectorCollection insertAll(List<Vector> myObjects) {
		super.insertAll(myObjects);
		List<Vector> vals = new ArrayList<Vector>(values());
		vpt = new VantagePointTree<Vector>(vals);
		return this;
	}
}
