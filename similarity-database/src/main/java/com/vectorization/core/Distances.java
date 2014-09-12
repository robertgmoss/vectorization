package com.vectorization.core;

public class Distances {

	public static double structuralEntropicDistance(SSVector x, SSVector y) {
		if (x.dimensionality() != y.dimensionality()) throw new SSException(
				"dimensionality must be equal");
		double sum = 0.0;
		for (int i = 0; i < x.dimensionality(); i++) {
			double a = x.get(i);
			double b = y.get(i);
			if (a == 0 || b == 0) continue;
			sum += (a + b) * log2(a + b) - (a * log2(a) + b * log2(b));

		}
		return Math.pow(2, 1 - 0.5 * sum) - 1;
	}

	private static double log2(double x) {
		return x == 0.0 ? 0.0 : Math.log(x) / Math.log(2);
	}

}
