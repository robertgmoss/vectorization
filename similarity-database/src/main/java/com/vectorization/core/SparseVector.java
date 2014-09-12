package com.vectorization.core;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SparseVector extends AbstractVector {

	private static final long serialVersionUID = -6348541795286382173L;
	private Map<Integer, Double> v = new LinkedHashMap<Integer, Double>();
	private int dimensionality;

	public SparseVector(String id, Double... data) {
		this(id, Arrays.asList(data));
	}

	public SparseVector(String id, List<Double> data) {
		super(id);
		dimensionality = data.size();
		for (int i = 0; i < dimensionality; i++) {
			if (data.get(i) != 0.0) {
				v.put(i, data.get(i));
			}
		}
	}

	public int dimensionality() {
		return dimensionality;
	}

	public double get(int i) {
		if (!v.containsKey(i)) return 0;
		return v.get(i);
	}

	public double distance(SSVector other) {
		return Distances.structuralEntropicDistance(
				Vectors.createNormalisedVector(this), Vectors.createNormalisedVector(other));
	}

}
