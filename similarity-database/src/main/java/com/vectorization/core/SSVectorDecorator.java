package com.vectorization.core;

/**
 * Provides a hook to allow vectors to be transformed whilst keeping
 * existing vectors in their original form.  Classes override the public
 * methods to provide new behaviour.
 * 
 * @author Robert Moss
 *
 */
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
