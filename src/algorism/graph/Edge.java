package algorism.graph;

public class Edge<T extends Comparable<T>> implements Comparable<Edge<T>> {
	// basic properties
	private Vertex<T> srcVertex;
	private Vertex<T> destVertex;
	private GraphKind type;
	private Double weight;

	// flow properties
	boolean argumentEdge;// marked as argument path edge
	Double forwardFlow;
	Double backwardFlow;// maybe exists in the backward edge(dest->src)

	public Edge() {
		this(GraphKind.DIRECTED, null);
	}

	public Edge(GraphKind type) {
		this(type, null);
	}

	public Edge(GraphKind type, Double weight) {
		this.type = type;
		this.weight = weight;
	}

	public Edge(GraphKind type, Double weight, Vertex<T> src, Vertex<T> dest) {
		this.type = type;
		this.weight = weight;
		setSrcVertex(src);
		setDestVertex(dest);
	}

	public Vertex<T> getSrcVertex() {
		return srcVertex;
	}

	/**
	 * to set the src vertex to this edge, and update connection relations of vertexes
	 * 
	 * @param srcVertex
	 */
	public void setSrcVertex(Vertex<T> srcVertex) {
		this.srcVertex = srcVertex;
		// srcVertex.addEdge(this);
		updateVertex();
	}

	public Vertex<T> getDestVertex() {
		return destVertex;
	}

	/**
	 * to set the dest vertex to this edge, and update connection relations of vertexes
	 * 
	 * @param destVertex
	 */
	public void setDestVertex(Vertex<T> destVertex) {
		this.destVertex = destVertex;
		// destVertex.addEdge(this);
		updateVertex();
	}

	/**
	 * update connection relations of vertexes
	 */
	public void updateVertex() {
		if (this.srcVertex != null && this.destVertex != null && this.srcVertex != this.destVertex) {
			this.srcVertex.addToNighbor(destVertex);
			this.destVertex.addFroNighbor(this.srcVertex);
			this.srcVertex.addEdge(this);
			if (this.type.is2Way()) {
				this.destVertex.addToNighbor(this.srcVertex);
				this.srcVertex.addFroNighbor(this.destVertex);
				this.destVertex.addEdge(this);
			}
		}
	}

	/**
	 * to judge if this edge runs from src to dest
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean isEdgeOf2V(Vertex<T> src, Vertex<T> dest) {
		return (this.srcVertex == src && this.destVertex == dest);
	}

	/**
	 * to judge if this edge runs from v1 to v2 or runs from v2 to v1
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public boolean isEdgeOf2V2(Vertex<T> v1, Vertex<T> v2) {
		return (this.srcVertex == v1 && this.destVertex == v2 || this.srcVertex == v2 && this.destVertex == v1);
	}

	public GraphKind getType() {
		return type;
	}

	public void setType(GraphKind type) {
		this.type = type;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String toString() {
		return "Edge(" + weight + ")" + srcVertex + (this.type.is2Way() ? "--" : "->") + destVertex;
	}

	public int compareTo(Edge<T> o) {
		int wc = this.weight.compareTo(o.weight);
		if (wc == 0) {
			int sc = this.srcVertex.compareTo(o.srcVertex);
			if (sc == 0) {
				return this.destVertex.compareTo(o.destVertex);
			} else {
				return sc;
			}
		}
		return wc;
	}
}
