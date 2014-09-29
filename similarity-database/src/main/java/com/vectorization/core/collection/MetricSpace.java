package com.vectorization.core.collection;

import java.io.Serializable;
import java.util.List;

import com.vectorization.core.Metric;

public interface MetricSpace<E extends Metric<E>> extends Serializable{
	
	MetricSpace<E> insertAll(E... myObjects);
	
	MetricSpace<E> insertAll(MetricSpace<E> myObjects);
	
	MetricSpace<E> retrieveKnn(int k, E prototype);
	
	List<E> values();

}
