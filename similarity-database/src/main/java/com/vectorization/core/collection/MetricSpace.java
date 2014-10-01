package com.vectorization.core.collection;

import java.io.Serializable;
import java.util.List;

import com.vectorization.core.Metric;
import com.vectorization.core.Vector;

public interface MetricSpace<E extends Metric<E>> extends Serializable{
	
	MetricSpace<E> insertAll(E... myObjects);
	
	MetricSpace<E> insertAll(MetricSpace<E> myObjects);
	
	Iterable<Vector> retrieveKnn(int k, E prototype);
	
	List<E> values();

}
