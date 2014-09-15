package com.vectorization.core;

import java.util.List;

/**
 * A convenience class for creating vectors in the system.
 * 
 * @author Robert Moss
 *
 */
public class Vectors {

	public static SSVector createNormalisedVector(String id, Double... ds) {
		return new NormalisedVector(createVector(id, ds));
	}

	public static SSVector createNormalisedVector(SSVector v) {
		return new NormalisedVector(v);
	}

	public static SSVector createVector(String id, Double... ds) {
		return new SparseVector(id, ds);
	}

	public static SSVector createVector(String id, List<Double> ds) {
		return new SparseVector(id, ds);
	}

	public static SSVector parseVector(String v) {
		String s = v.substring(v.indexOf("[") + 1, v.indexOf("]"));
		if (s.equals("")) return createVector("");
		String[] dims = s.split(",");
		Double[] ds = new Double[dims.length];
		for (int i = 0; i < dims.length; i++) {
			ds[i] = Double.parseDouble(dims[i].trim());
		}
		return createVector("", ds);
	}

	public static void main(String[] args) {
		SSVector vector = createVector("1", 1.1, 2.1, 0.0);
		System.out.println(vector);
		SSVector parseVector = parseVector(vector.toString());
		System.out.println(parseVector);
		System.out.println(createNormalisedVector(parseVector));

		vector = createVector("-1");
		System.out.println(vector);
		parseVector = parseVector(vector.toString());
		System.out.println(parseVector);
		System.out.println(createNormalisedVector(parseVector));

	}
}
