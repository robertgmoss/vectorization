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
 * Provides a hook to allow vectors to be transformed whilst keeping
 * existing vectors in their original form.  Classes override the public
 * methods to provide new behaviour.
 * 
 * @author Robert Moss
 *
 */
public abstract class SSVectorDecorator extends AbstractVector {

	private static final long serialVersionUID = 9039416037284202187L;
	private SSVector delegate;

	public SSVectorDecorator(SSVector vector) {
		super(vector.id());
		this.delegate = vector;
	}

	public int dimensionality() {
		return delegate.dimensionality();
	}

	public double get(int i) {
		return delegate.get(i);
	}

	public double distance(SSVector other) {
		return delegate.distance(other);
	}

}
