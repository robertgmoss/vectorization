package com.vectorization.core.collection;

import java.util.ArrayList;
import java.util.List;

import com.vectorization.core.Vector;
import com.vectorization.core.vectors.Vectors;

public class VPTVectorSpace extends AbstractVectorSpace {

	private static final long serialVersionUID = 7778317446717284965L;
	private VantagePointTree<Vector> vpt;

	public VPTVectorSpace(int dimensionality, Vector... points) {
		super(dimensionality);
		insertAll(points);
	}

	public VectorSpace retrieveKnn(int k, Vector prototype) {
		double threshold = k*Vectors.MAX_DISTANCE / (size()*size());
		//System.out.print("VPTCollection.retriveKnn: ");
		List<Vector> result = vpt.rangeQuery(prototype, threshold);
		//System.out.println(result.size());
		while (threshold <= Vectors.MAX_DISTANCE && result.size() < k) {
			threshold += threshold;
			result = vpt.rangeQuery(prototype, threshold);
		}		
		SimpleVectorSpace sp = new SimpleVectorSpace(dimensionality());
		sp.insertAll(result);
		return sp.retrieveKnn(k, prototype);
	}
	
	@Override
	public VectorSpace removeAll(String... vectorNames) {
		super.removeAll(vectorNames);
		vpt = new VantagePointTree<Vector>(super.values());
		return this;
	}
	
	@Override
	public VectorSpace insertAll(Iterable<Vector> myObjects) {
		super.insertAll(myObjects);
		List<Vector> vals = new ArrayList<Vector>(values());
		vpt = new VantagePointTree<Vector>(vals);
		return this;
	}
}
