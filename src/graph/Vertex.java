package graph;

import java.util.Iterator;
import java.util.LinkedList;

public class Vertex<T extends Comparable<T>> implements Comparable<Vertex<T>> {
	// basic properties
	private T data;
	private LinkedList<Edge<T>> links; // edges linked with this vertex, those start from this vertex
	private LinkedList<Vertex<T>> toNighbors; // vertexes this vertex go to, when graph is undirected, same as
												// froNighbors
	private LinkedList<Vertex<T>> froNighbors; // vertexes go to this vertex, when graph is undirected, same as
												// noNighbors

	// properties for search(traversing or iterator)
	boolean known;
	int dist;
	Vertex<T> path;// it means: (this.path -> this) is a path

	public Vertex() {
		this(null);
	}

	public Vertex(T data) {
		this.data = data;
		links = new LinkedList<Edge<T>>();
		toNighbors = new LinkedList<Vertex<T>>();
		froNighbors = new LinkedList<Vertex<T>>();
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * to get out-degree, i.e. the number of vertexes this vertex go to
	 * 
	 * @return
	 */
	public int getOutDegree() {
		return toNighbors.size();
	}

	/**
	 * to get in-degree, i.e. the number of vertexes come to this vertex
	 * 
	 * @return
	 */
	public int getInDegree() {
		return froNighbors.size();
	}

	public LinkedList<Edge<T>> getLinks() {
		return links;
	}

	public void setLinks(LinkedList<Edge<T>> links) {
		this.links = links;
	}

	/**
	 * to add one edge that is linked with this vertex
	 * 
	 * @param edge
	 */
	public void addEdge(Edge<T> edge) {
		if (this.links == null) {
			this.links = new LinkedList<Edge<T>>();
		}
		if (!this.links.contains(edge)) {
			this.links.add(edge);
		}
	}

	public LinkedList<Vertex<T>> getToNighbors() {
		return toNighbors;
	}

	public void setToNighbors(LinkedList<Vertex<T>> toNighbors) {
		this.toNighbors = toNighbors;
	}

	public LinkedList<Vertex<T>> getFroNighbors() {
		return froNighbors;
	}

	public void setFroNighbors(LinkedList<Vertex<T>> froNighbors) {
		this.froNighbors = froNighbors;
	}

	/**
	 * return a list that contains all the vertexes is adjacent to this vertex
	 * 
	 * @return
	 */
	public LinkedList<Vertex<T>> getAllNighbors() {
		LinkedList<Vertex<T>> list = new LinkedList<Vertex<T>>();
		list.addAll(toNighbors);
		Iterator<Vertex<T>> itr = froNighbors.iterator();
		while (itr.hasNext()) {
			Vertex<T> v = itr.next();
			if (!list.contains(v)) {
				list.add(v);
			}
		}
		return list;
	}

	/**
	 * to add one vertex which this vertex go to
	 * 
	 * @param dest
	 */
	public void addToNighbor(Vertex<T> dest) {
		if (this.toNighbors == null) {
			this.toNighbors = new LinkedList<Vertex<T>>();
		}
		if (!this.toNighbors.contains(dest)) {
			this.toNighbors.add(dest);
		}
	}

	/**
	 * to add one vertex which come to this vertex
	 * 
	 * @param from
	 */
	public void addFroNighbor(Vertex<T> from) {
		if (this.froNighbors == null) {
			this.froNighbors = new LinkedList<Vertex<T>>();
		}
		if (!this.froNighbors.contains(from)) {
			this.froNighbors.add(from);
		}
	}

	public String toString() {
		return "Vertex(" + data + ")";
	}

	/**
	 * to get this vertex string and also linked vertex's name
	 * 
	 * @return
	 */
	public String toString2() {
		Iterator<Vertex<T>> itr = toNighbors.iterator();
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		while (itr.hasNext()) {
			sb.append(itr.next().getData());
			if (itr.hasNext()) {
				sb.append(',');
			}
		}
		sb.append(']');
		return toString() + sb.toString();
	}

	public int compareTo(Vertex<T> o) {
		return this.data.compareTo(o.data);
	}
}
