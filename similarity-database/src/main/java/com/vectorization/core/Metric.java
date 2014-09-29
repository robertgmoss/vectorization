package com.vectorization.core;

public interface Metric<M extends Metric<M>> {

	double distance(M other);
}
