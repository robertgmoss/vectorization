package com.vectorization.core;

public class NormalisedVector extends SSVectorDecorator {

	private static final long serialVersionUID = -7461934269703184566L;
	private double sum = 0.0;

	public NormalisedVector(SSVector vector) {
		super(vector);
		for (int i = 0; i < dimensionality(); i++) {
			sum += super.get(i);
		}
	}

	@Override
	public double get(int i) {
		return super.get(i) / sum;
	}

	public double distance(SSVector other) {
		return super.distance(other);
	}

}
