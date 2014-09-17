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
package com.vectorization.util;

import java.util.Collections;
import java.util.List;

import com.vectorization.core.SSVector;

/**
 * Provides methods for manipulating a collection of vectors
 * 
 * @author Robert Moss
 *
 */
public class Partition {

	public static <P extends SSVector> int select(P p, P[] points, int i, int j) {
		int mid = i + (j - i) / 2;
		int m = -1;
		while (i < j) {
			m = partition(p, points, i, j);
			if (m == mid) return m;
			if (m > mid) j = m - 1;
			if (m < mid) i = m + 1;
		}
		return m;
	}
	
	public static <P extends SSVector> int select(P p, List<P> points, int i, int j) {
		int mid = i + (j - i) / 2;
		int m = -1;
		while (i <= j) {
			m = partition(p, points, i, j);
			if (m == mid) return m;
			if (m > mid) j = m - 1;
			if (m < mid) i = m + 1;
		}
		return m;
	}

	private static <P extends SSVector> int partition(P p, P[] points, int i,
			int j) {
		if (i > j) return -1; // 0 elements
		if (i == j) return i; // one element
		// now, for the remaining, partition
		double d = p.distance(points[j]);
		int l = i;
		for (int r = i; r < j; r++) {
			if (p.distance(points[r]) <= d) {
				swap(points, l, r);
				l++;
			}
		}
		swap(points, l, j);
		return l;
	}
	
	private static <P extends SSVector> int partition(P p, List<P> points, int i,
			int j) {
		if (i > j) return -1; // 0 elements
		if (i == j) return i; // one element
		// now, for the remaining, partition
		double d = p.distance(points.get(j));
		int l = i;
		for (int r = i; r < j; r++) {
			if (p.distance(points.get(r)) <= d) {
				swap(points, l, r);
				l++;
			}
		}
		swap(points, l, j);
		return l;
	}

	private static <P extends SSVector> void swap(P[] points, int l, int r) {
		P t = points[r];
		points[r] = points[l];
		points[l] = t;
	}
	
	private static <P extends SSVector> void swap(List<P> points, int l, int r) {
		Collections.swap(points, l, r);
	}

}
