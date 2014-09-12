package com.vectorization.core;

import java.io.Serializable;

public interface SSVector extends Serializable {

	String id();

	double get(int i);

	int dimensionality();

	double distance(SSVector other);
}
