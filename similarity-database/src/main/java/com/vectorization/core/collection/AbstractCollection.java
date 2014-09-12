package com.vectorization.core.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vectorization.core.SSException;
import com.vectorization.core.SSVector;

public abstract class AbstractCollection<E extends SSVector> implements
		SSCollection<E> {

	private static final long serialVersionUID = -1655815257490860488L;
	private Map<String, E> vectors = new LinkedHashMap<String, E>();
	private int dimensionality;

	public AbstractCollection(int dimensionality) {
		this.dimensionality = dimensionality;
	}

	public int dimensionality() {
		return dimensionality;
	}
	
	public SSVector get(String vectorName) {
		if(!vectors.containsKey(vectorName)) throw new SSException("");
		return vectors.get(vectorName);
	}
	
	public SSCollection<E> insert(E myObject) {
		if (myObject.dimensionality() != this.dimensionality()) throw new SSException(
				"Cannot insert element with dimensionality: "
						+ myObject.dimensionality()
						+ " into collection with dimensionality: "
						+ this.dimensionality());
		vectors.put(myObject.id(), myObject);
		return this;
	}
	
	public Iterator<E> iterator() {
		return vectors.values().iterator();
	}
	
	public Collection<E> values(){
		return vectors.values();
	}
	
	public boolean contains(SSVector o) {
		return vectors.containsKey(o.id());
	}

	public int size() {
		return vectors.size();
	}

	@Override
	public String toString() {
		if (size() == 0) return "empty";
		StringBuilder sb = new StringBuilder();
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext()) sb.append("\n");
		}
		return sb.toString();
	}

}
