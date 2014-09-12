package com.vectorization.core.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vectorization.core.SSVector;

public class Set<E extends SSVector> extends AbstractCollection<E> {

	private static final long serialVersionUID = 2751516171619962956L;

	public Set(int dimensionality) {
		super(dimensionality);
	}

	public Set(int dimensionality, Collection<E> collection) {
		super(dimensionality);
		for (E e : collection) {
			insert(e);
		}
	}

	public SSCollection<E> retrieveKnn(int k, final SSVector prototype) {
		// do something like quick select instead...

		List<E> q = new ArrayList<E>(values());
		Collections.sort(q, new Comparator<E>() {

			public int compare(E o1, E o2) {
				double d1 = o1.distance(prototype);
				// System.out.println("d(" + o1 + ", " + prototype + ")=" + d1);
				double d2 = o2.distance(prototype);
				// System.out.println("d(" + o2 + ", " + prototype + ")=" + d2);
				return d1 < d2 ? -1 : d1 == d2 ? 0 : +1;
			}
		});
		return new Set<E>(this.dimensionality(), q.subList(0, k));
	}

}
