package com.vectorization.core;

import java.io.Serializable;

/**
 * The type of all vectors that operate in the system.
 * 
 * @author Robert Moss
 *
 */
public interface SSVector extends Serializable {

	String id();

	double get(int i);

	int dimensionality();

	double distance(SSVector other);
}
