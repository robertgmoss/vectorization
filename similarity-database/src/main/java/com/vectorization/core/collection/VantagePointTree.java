package com.vectorization.core.collection;

import java.util.Iterator;

import com.vectorization.core.SSVector;

/**
 * Stores the vectors in such a way as to provide fast lookup using the retrieveKnn method.
 * It implements the vantage point tree algorithm which is a binary-tree that recursively stores
 * in its left subtree all elements that have a distance less than the median distance to its root
 * and in the right subtree all other elements.
 * 
 * @author Robert Moss
 *
 * @param <E>
 */
public class VantagePointTree<E extends SSVector> implements SSCollection<E> {

	public SSCollection<E> insert(E myObject) {
		return this;
	}

	public SSCollection<E> retrieveKnn(int k, E prototype) {
		return this;
	}

	public Iterator<E> iterator() {
		return new Iterator<E>() {

			public boolean hasNext() {
				// TODO Auto-generated method stub
				return false;
			}

			public E next() {
				// TODO Auto-generated method stub
				return null;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	public boolean contains(SSVector o) {
		// TODO Auto-generated method stub
		return false;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int dimensionality() {
		// TODO Auto-generated method stub
		return 0;
	}

	public SSVector get(String vectorName) {
		// TODO Auto-generated method stub
		return null;
	}

}
