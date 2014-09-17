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
package com.vectorization.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vectorization.core.SSVector;
import com.vectorization.core.Vectors;
import com.vectorization.util.Partition;

/**
 * 
 * Stores the vectors in such a way as to provide fast lookup using the retrieveKnn method.
 * It implements the vantage point tree algorithm which is a binary-tree that recursively stores
 * in its left subtree all elements that have a distance less than the median distance to its root
 * and in the right subtree all other elements.  It sacrifices insert time to provide fast lookup.
 * 
 * @author Robert Moss
 *
 * @param <E>
 */
public class VantagePointTree<E extends SSVector> extends  AbstractCollection<E> {
	
	private static final long serialVersionUID = 4636526503042865232L;
	private Node root;

	private class Node implements Serializable{

		private static final long serialVersionUID = -4605853971019390873L;
		E data;
		double mu;
		Node left;
		Node right;
	}
	
	public VantagePointTree(int dimensionality, E... points) {
		super(dimensionality);
		root = build(Arrays.asList(points), 0, points.length - 1);
	}
	
	private Node build(List<E> points, int i, int j) {
		if (i > j) return null;
		Node n = new Node();
		n.data = points.get(j);
		if (i == j) return n;
		int p = Partition.select(n.data, points, i, j - 1);
		n.mu = n.data.distance(points.get(p));
		n.left = build(points, i, p);
		n.right = build(points, p + 1, j - 1);
		return n;
	}
	
	@Override
	public SSCollection<E> remove(String vectorName) {
		super.remove(vectorName);
		List<E> vals = new ArrayList<E>(values());
		root = build(vals,  0, vals.size() - 1);
		return this;
	}
	
	@Override
	public SSCollection<E> removeAll(String... vectorNames) {
		super.removeAll(vectorNames);
		List<E> vals = new ArrayList<E>(values());
		root = build(vals,  0, vals.size() - 1);
		return this;
	}

	public SSCollection<E> insert(E myObject) {
		super.insert(myObject);
		// values() now also contains inserted
		List<E> vals = new ArrayList<E>(values());
		root = build(vals,  0, vals.size() - 1);
		return this;
	}
	
	public SSCollection<E> insertAll(E... myObjects) {
		for(E e : myObjects){
			super.insert(e);
		}
		List<E> vals = new ArrayList<E>(values());
		root = build(vals,  0, vals.size() - 1);
		return this;
	}

	public SSCollection<E> retrieveKnn(int k, E prototype) {
		double threshold = Vectors.MAX_DISTANCE / size();
		double increment = 0.0;
		SSCollection<E> result = rangeQuery(prototype, threshold);
		while(threshold <= Vectors.MAX_DISTANCE && result.size() < k){
			threshold = threshold + increment;
			result = rangeQuery(prototype, threshold);
			increment = threshold - increment;
		}
		System.out.println("resorting to brute force on size " + result.size());
		// now that we have narrowed down the search
		// we can just do a naive search since
		// result is of type Space.
		return result.retrieveKnn(k, prototype);
	}
	
	public SSCollection<E> rangeQuery(E query, double threshold) {
		SSCollection<E> points = new Space<E>(this.dimensionality());
		rangeQuery(query, threshold, points, root);
		return points;
	}

	private void rangeQuery(E query, double threshold, SSCollection<E> points,
			Node node) {
		if (node == null) return;
		E p = node.data;
		double d = p.distance(query);
		if (d <= threshold) {
			points.insert(node.data);
		}
		if (d - threshold <= node.mu) rangeQuery(query, threshold, points, node.left);
		if (d + threshold > node.mu) rangeQuery(query, threshold, points, node.right);
	}

}
