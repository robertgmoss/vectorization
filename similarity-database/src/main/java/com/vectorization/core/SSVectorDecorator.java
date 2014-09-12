package com.vectorization.core;

public abstract class SSVectorDecorator extends AbstractVector {

	private static final long serialVersionUID = 9039416037284202187L;
	private SSVector delegate;

	public SSVectorDecorator(SSVector vector) {
		super(vector.id());
		this.delegate = vector;
	}

	public int dimensionality() {
		return delegate.dimensionality();
	}

	public double get(int i) {
		return delegate.get(i);
	}

	public double distance(SSVector other) {
		return delegate.distance(other);
	}

}
