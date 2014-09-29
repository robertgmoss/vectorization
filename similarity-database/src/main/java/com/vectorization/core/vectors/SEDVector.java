package com.vectorization.core.vectors;

import java.util.List;

import com.vectorization.core.Distances;
import com.vectorization.core.SparseVector;
import com.vectorization.core.Vector;

public class SEDVector extends SparseVector{

	private static final long serialVersionUID = -6188056848964224598L;

	public SEDVector(String id, Double[] data) {
		super(id, data);
	}
	
	public SEDVector(String id, List<Double> ds) {
		super(id, ds);
	}

	public double distance(Vector other) {
		return Distances.structuralEntropicDistance(
				Vectors.createNormalisedVector(this), Vectors.createNormalisedVector(other));
	}

}
