package com.vectorization.core.collection;

import java.util.Collection;

import com.vectorization.core.SSVector;

public class VantagePointForest<E extends SSVector> extends CompositeCollection<E>{

	private static final long serialVersionUID = -1947039174911459830L;

	public VantagePointForest(int dimensionality) {
		super(dimensionality);
	}

	public VantagePointForest(int dimensionality,
			Collection<SSCollection<E>> value) {
		super(dimensionality, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SSCollection<E> newCollection() {
		return new VantagePointTree<E>(dimensionality());
	}

}
