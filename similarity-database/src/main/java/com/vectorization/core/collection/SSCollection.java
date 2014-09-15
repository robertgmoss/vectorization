package com.vectorization.core.collection;

import java.io.Serializable;

import com.vectorization.core.SSVector;

/**
 * Provides the type for collections that are stored in the system.
 * 
 * @author Robert Moss
 *
 * @param <E>
 */
public interface SSCollection<E extends SSVector> extends Iterable<E>,
		Serializable {

	SSCollection<E> insert(E myObject);

	SSCollection<E> retrieveKnn(int k, E prototype);

	boolean contains(SSVector o);

	int size();

	int dimensionality();

	SSVector get(String vectorName);

}
