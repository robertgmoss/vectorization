package com.vectorization.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vectorization.core.Metric;
import com.vectorization.util.Partition;

public class VantagePointTree<M extends Metric<M>> implements Serializable{

	private static final long serialVersionUID = -1239205914549364409L;
	private Node root;

	private class Node implements Serializable {

		private static final long serialVersionUID = -4605853971019390873L;
		M data;
		double mu;
		Node left;
		Node right;
		int size;
	}

	public VantagePointTree(M... points) {
		root = build(Arrays.asList(points), 0, points.length - 1);
	}

	public VantagePointTree(List<M> points) {
		root = build(points, 0, points.size() - 1);
	}

	private Node build(List<M> points, int i, int j) {
		if (i > j)
			return null;
		Node n = new Node();
		n.data = points.get(j);
		n.size = 1;
		if (i == j)
			return n;
		int p = Partition.select(n.data, points, i, j - 1);
		n.mu = n.data.distance(points.get(p));

		n.left = build(points, i, p);
		int leftSize = 0;
		if (n.left != null)
			leftSize = n.left.size;

		n.right = build(points, p + 1, j - 1);
		int rightSize = 0;
		if (n.right != null)
			rightSize = n.right.size;

		n.size = leftSize + rightSize;
		return n;
	}

	public List<M> values() {
		return values(root, new ArrayList<M>());
	}

	private List<M> values(Node root, List<M> coll) {
		if (root == null)
			return coll;
		coll.add(root.data);
		values(root.left, coll);
		values(root.right, coll);
		return coll;
	}

	public VantagePointTree<M> insertAll(M... myObjects) {
		return insertAll(Arrays.asList(myObjects));
	}

	public VantagePointTree<M> insertAll(List<M> myObjects) {
		List<M> vals = new ArrayList<M>(values());
		vals.addAll(myObjects);
		root = build(vals, 0, vals.size() - 1);
		return this;
	}

	public VantagePointTree<M> insertAll(MetricSpace<M> myObjects) {
		insertAll(myObjects.values());
		return this;
	}

	public int size() {
		return root == null ? 0 : root.size;
	}

	public List<M> rangeQuery(M query, double threshold) {
		List<M> points = new ArrayList<M>();
		rangeQuery(query, threshold, points, root);
		return points;
	}

	private void rangeQuery(M query, double threshold, List<M> points, Node node) {
		if (node == null)
			return;
		M p = node.data;
		double d = p.distance(query);
		if (d <= threshold) {
			points.add(node.data);
		}
		if (d - threshold <= node.mu)
			rangeQuery(query, threshold, points, node.left);
		if (d + threshold > node.mu)
			rangeQuery(query, threshold, points, node.right);
	}

}
