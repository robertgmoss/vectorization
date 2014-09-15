package com.vectorization.core;
/**
 * Provides default methods common to implementing classes of SSVector.
 * 
 * @author Robert Moss
 *
 */
public abstract class AbstractVector implements SSVector {

	private static final long serialVersionUID = -1714510626632574674L;
	private String id;

	public AbstractVector(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimensionality();
		for (int i = 0; i < dimensionality(); i++) {
			result = prime * result + Double.valueOf(get(i)).hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof SSVector)) { return false; }
		SSVector other = (SSVector) obj;
		if (this.dimensionality() != other.dimensionality()) return false;
		for (int i = 0; i < dimensionality(); i++) {
			if (this.get(i) != other.get(i)) return false;
		}
		return true;
	}

	public String id() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < dimensionality(); i++) {
			sb.append(get(i));
			if (i < dimensionality() - 1) sb.append(", ");
		}
		sb.append("]");
		return String.format("%15s = %s", this.id(), sb.toString());
	}
}
