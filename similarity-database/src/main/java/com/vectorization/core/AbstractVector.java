/*  
 *  Copyright (C) 2014 Robert Moss
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
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
