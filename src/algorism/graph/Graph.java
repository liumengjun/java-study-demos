package algorism.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Graph<T extends Comparable<T>> {
	private static final double INFINITY = Double.MAX_VALUE;
	private ArrayList<Vertex<T>> vertexes;
	private ArrayList<Edge<T>> edges;
	private int vertexCount;
	private int edgeCount;
	private GraphKind type;

	public Graph() {
		this(GraphKind.DIRECTED);
	}

	public Graph(GraphKind type) {
		this.type = type;
		vertexes = new ArrayList<Vertex<T>>();
		edges = new ArrayList<Edge<T>>();
		vertexCount = 0;
		edgeCount = 0;
	}

	public ArrayList<Vertex<T>> getVertexes() {
		return vertexes;
	}

	public void setVertexes(ArrayList<Vertex<T>> vertexes) {
		this.vertexes = vertexes;
	}

	public ArrayList<Edge<T>> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge<T>> edges) {
		this.edges = edges;
	}

	/**
	 * to add one edge into this graph, and add junctural vertexes into this graph as well
	 * 
	 * @param edge
	 */
	public void addEdge(Edge<T> edge) {
		Vertex<T> src = edge.getSrcVertex();
		Vertex<T> dest = edge.getDestVertex();
		if (src == null || dest == null || src == dest) {
			return;
		}
		if (!this.edges.contains(edge)) {
			this.edges.add(edge);
			this.edgeCount++;
			if (!this.vertexes.contains(src)) {
				this.addVertex(src);
			}
			if (!this.vertexes.contains(dest)) {
				this.addVertex(dest);
			}
		}
	}

	/**
	 * to add one edge that directs from src to dest, with a weight
	 * 
	 * @param src
	 * @param dest
	 * @param weight
	 */
	public void addEdge(Vertex<T> src, Vertex<T> dest, Double weight) {
		if (src == null || dest == null || src == dest) {
			return;
		}
		Edge<T> edge = new Edge<T>(this.getType(), weight, src, dest);
		this.addEdge(edge);
	}

	/**
	 * to add one vertex into this graph, and add junctural edges into this graph as well
	 * 
	 * @param vertex
	 */
	public void addVertex(Vertex<T> vertex) {
		if (!this.vertexes.contains(vertex)) {
			this.vertexes.add(vertex);
			this.vertexCount++;
			LinkedList<Edge<T>> linkedEdges = vertex.getLinks();
			Iterator<Edge<T>> itr = linkedEdges.iterator();
			while (itr.hasNext()) {
				Edge<T> edge = itr.next();
				if (!this.edges.contains(edge)) {
					this.addEdge(edge);
				}
			}
		}
	}

	/**
	 * get the vertex of data from the graph
	 * <p>
	 * NOTE: we take for granted that data is unique identifier of each vertex
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public Vertex<T> getVertexByData(T data) {
		Iterator<Vertex<T>> itr = vertexes.iterator();
		while (itr.hasNext()) {
			Vertex<T> v = itr.next();
			if (v.getData().compareTo(data) == 0) {
				return v;
			}
		}
		return null;
	}

	/**
	 * add vertex of data into this graph
	 * <p>
	 * NOTE: we take for granted that data is unique identifier of each vertex
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public Vertex<T> addVertex(T data) {
		Vertex<T> v = getVertexByData(data);
		if (v != null) {
			return v;
		}
		v = new Vertex<T>(data);
		this.vertexes.add(v);
		this.vertexCount++;
		return v;
	}

	/**
	 * add one edge, which goes from data1 to data2, into this graph
	 * <p>
	 * NOTE: we take for granted that data is unique identifier of each vertex
	 * </p>
	 * 
	 * @param data1
	 * @param data2
	 * @param weight
	 */
	public void addEdge(T data1, T data2, Double weight) {
		Vertex<T> v1 = addVertex(data1);
		Vertex<T> v2 = addVertex(data2);
		Edge<T> edge = new Edge<T>(this.getType(), weight, v1, v2);
		this.edges.add(edge);
		this.edgeCount++;
	}

	/**
	 * to get the weight which is marked from src to dest
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public Double getWeightOf2V(Vertex<T> src, Vertex<T> dest) {
		Double weight = null;
		LinkedList<Edge<T>> toEdges = src.getLinks();
		Iterator<Edge<T>> itr = toEdges.iterator();
		while (itr.hasNext()) {
			Edge<T> edge = itr.next();
			if (edge.getDestVertex() == dest) {
				return edge.getWeight();
			}
		}
		return weight;
	}

	/**
	 * to get the weight which is set to the edge from v1 to v2 or the edge from v2 to v1
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public Double getWeightOf2V2(Vertex<T> v1, Vertex<T> v2) {
		Double weight = null;
		LinkedList<Edge<T>> toEdges = v1.getLinks();
		Iterator<Edge<T>> itr = toEdges.iterator();
		while (itr.hasNext()) {
			Edge<T> edge = itr.next();
			if (edge.getDestVertex() == v2) {
				return edge.getWeight();
			}
		}
		LinkedList<Edge<T>> froEdges = v2.getLinks();
		Iterator<Edge<T>> itr2 = froEdges.iterator();
		while (itr2.hasNext()) {
			Edge<T> edge = itr2.next();
			if (edge.getDestVertex() == v1) {
				return edge.getWeight();
			}
		}
		return weight;
	}

	/**
	 * to get the edge which is marked from src to dest
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public Edge<T> findEdgeOf2V(Vertex<T> src, Vertex<T> dest) {
		Edge<T> e = null;
		LinkedList<Edge<T>> toEdges = src.getLinks();
		Iterator<Edge<T>> itr = toEdges.iterator();
		while (itr.hasNext()) {
			Edge<T> edge = itr.next();
			if (edge.getDestVertex() == dest) {
				return edge;
			}
		}
		return e;
	}

	/**
	 * to find the edge which runs from v1 to v2 or from v2 to v1
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public Edge<T> findEdgeOf2V2(Vertex<T> v1, Vertex<T> v2) {
		Edge<T> e = null;
		LinkedList<Edge<T>> toEdges = v1.getLinks();
		Iterator<Edge<T>> itr = toEdges.iterator();
		while (itr.hasNext()) {
			Edge<T> edge = itr.next();
			if (edge.getDestVertex() == v2) {
				return edge;
			}
		}
		LinkedList<Edge<T>> froEdges = v2.getLinks();
		Iterator<Edge<T>> itr2 = froEdges.iterator();
		while (itr2.hasNext()) {
			Edge<T> edge = itr2.next();
			if (edge.getDestVertex() == v1) {
				return edge;
			}
		}
		return e;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public int getEdgeCount() {
		return edgeCount;
	}

	public void setEdgeCount(int edgeCount) {
		this.edgeCount = edgeCount;
	}

	public GraphKind getType() {
		return type;
	}

	public void setType(GraphKind type) {
		this.type = type;
	}

	/**
	 * to generate a topological sequence. for a directed no-circle graph
	 * 
	 * @return
	 * @throws CycleFoundException
	 */
	public ArrayList<Vertex<T>> topSort() throws CycleFoundException {
		ArrayList<Vertex<T>> topSort = new ArrayList<Vertex<T>>();

		Queue<Vertex<T>> q = new ArrayDeque<Vertex<T>>();
		Map<Vertex<T>, Integer> inDegreeMap = new HashMap<Vertex<T>, Integer>();
		// enqueue vertex has In-Degree is 0
		for (int i = 0; i < vertexes.size(); i++) {
			Vertex<T> v = vertexes.get(i);
			inDegreeMap.put(v, v.getInDegree());
			if (v.getInDegree() == 0) {
				q.add(v);
			}
		}

		int counter = 0;
		while (!q.isEmpty()) {
			// add v into the topological sequence
			Vertex<T> v = q.poll();
			topSort.add(v);
			counter++;

			LinkedList<Vertex<T>> toVs = v.getToNighbors();
			Iterator<Vertex<T>> itr = toVs.iterator();
			// for each Vertex w adjacent to v:
			// decrease its in-degree by 1, if in-degree is 0, then grab it
			while (itr.hasNext()) {
				Vertex<T> w = itr.next();
				int inDegree = inDegreeMap.get(w);
				inDegree--;
				inDegreeMap.put(w, inDegree);
				if (inDegree == 0) {
					q.add(w);
				}
			}
		}

		if (counter != this.getVertexCount()) {
			throw new CycleFoundException();
		}

		return topSort;
	}

	/**
	 * single source shortest path
	 * <p>
	 * this method uses the algorithm of Dijkstra, and the result can be shown by the method
	 * {@link #printPath(Vertex, Map, Map)}
	 * </p>
	 * 
	 * @param s
	 * @return map[]{distMap, pathMap}: the distance map and path map from s to other vertexes
	 */
	public Map<?, ?>[] sssp(Vertex<T> s) {
		Map<Vertex<T>, Double> distMap = new HashMap<Vertex<T>, Double>();
		Map<Vertex<T>, Vertex<T>> pathMap = new HashMap<Vertex<T>, Vertex<T>>();
		// to put the unknown vertexes
		Set<Vertex<T>> unknownVs = new HashSet<Vertex<T>>();
		// init
		for (int i = 0; i < vertexes.size(); i++) {
			Vertex<T> v = vertexes.get(i);
			distMap.put(v, INFINITY);
			pathMap.put(v, null);
			unknownVs.add(v);
		}
		distMap.put(s, 0.0);

		while (!unknownVs.isEmpty()) {
			// find the smallest distance vertex
			Vertex<T> v = null;
			double tempMinDist = INFINITY;
			Iterator<Vertex<T>> tempItr = unknownVs.iterator();
			while (tempItr.hasNext()) {
				Vertex<T> tempV = tempItr.next();
				if (v == null) {
					v = tempV;
					tempMinDist = distMap.get(v);
				} else {
					if (tempMinDist > distMap.get(tempV)) {
						v = tempV;
						tempMinDist = distMap.get(v);
					}
				}
			}
			unknownVs.remove(v);

			LinkedList<Vertex<T>> toVs = v.getToNighbors();
			Iterator<Vertex<T>> itr = toVs.iterator();
			// for each Vertex w adjacent to v:
			// to calculate the new smaller distance,
			// Min{v.dist + weight(v,w), w.dist}
			double vDist = distMap.get(v);
			while (itr.hasNext()) {
				Vertex<T> w = itr.next();
				// update path of w
				if (unknownVs.contains(w)) {
					Double vwWeight = getWeightOf2V(v, w);
					if (vwWeight == null) {
						continue;
					}
					double wDist = distMap.get(w);
					if (vDist + vwWeight < wDist) {
						wDist = vDist + vwWeight;
						distMap.put(w, wDist);
						pathMap.put(w, v);
					}
				}
			}
		}

		return new Map[] { distMap, pathMap };
	}

	/**
	 * only for the use to show the result of {@link #sssp(Vertex)}
	 * 
	 * @param s
	 * @param distMap
	 * @param pathMap
	 */
	public void printPath(Vertex<T> s, Map<Vertex<T>, Double> distMap, Map<Vertex<T>, Vertex<T>> pathMap) {
		Set<Entry<Vertex<T>, Double>> distEntrySet = distMap.entrySet();
		Iterator<Entry<Vertex<T>, Double>> distEntryItr = distEntrySet.iterator();
		while (distEntryItr.hasNext()) {
			Entry<Vertex<T>, Double> distEntry = distEntryItr.next();
			if (distEntry.getValue() != INFINITY) {
				Vertex<T> v = distEntry.getKey();
				if (v != s) {
					System.out.print(s + "->" + v + "=" + distEntry.getValue() + " : ");
					ArrayList<Vertex<T>> pathVs = new ArrayList<Vertex<T>>();
					Vertex<T> pre = v;
					while (pathMap.get(pre) != null) {
						pre = pathMap.get(pre);
						pathVs.add(pre);
					}
					for (int i = pathVs.size() - 1; i >= 0; i--) {
						System.out.print(pathVs.get(i) + "->");
					}
					System.out.println(v);
				}
			}
		}
	}

	/**
	 * the Prim's algorithm implement of minimum spanning tree.
	 * 
	 * @return all edges in the minimum spanning tree
	 */
	public ArrayList<Edge<T>> mstOfPrim() {
		// the minimum spanning tree's edges
		ArrayList<Edge<T>> mstEdges = new ArrayList<Edge<T>>();

		// to store the distance from known vertexes to unknown vertexes
		Map<Vertex<T>, Double> distMap = new HashMap<Vertex<T>, Double>();
		Map<Vertex<T>, Vertex<T>> pathMap = new HashMap<Vertex<T>, Vertex<T>>();
		// to put the unknown vertexes
		Set<Vertex<T>> unknownVs = new HashSet<Vertex<T>>();
		// init
		for (int i = 0; i < vertexes.size(); i++) {
			Vertex<T> v = vertexes.get(i);
			distMap.put(v, INFINITY);
			pathMap.put(v, null);
			unknownVs.add(v);
		}
		// to start from the vertex[0]
		Vertex<T> s = vertexes.get(0);
		distMap.put(s, 0.0);

		while (!unknownVs.isEmpty()) {
			// find the smallest distance vertex
			Vertex<T> v = null;
			double tempMinDist = INFINITY;
			Iterator<Vertex<T>> tempItr = unknownVs.iterator();
			while (tempItr.hasNext()) {
				Vertex<T> tempV = tempItr.next();
				if (v == null) {
					v = tempV;
					tempMinDist = distMap.get(v);
				} else {
					if (tempMinDist > distMap.get(tempV)) {
						v = tempV;
						tempMinDist = distMap.get(v);
					}
				}
			}
			unknownVs.remove(v);
			if (pathMap.get(v) != null) {
				mstEdges.add(findEdgeOf2V2(pathMap.get(v), v));
			}

			// the following procedure also applies to directed graph.
			LinkedList<Vertex<T>> linkVs = type.is2Way() ? v.getToNighbors() : v.getAllNighbors();
			Iterator<Vertex<T>> itr = linkVs.iterator();
			// for each Vertex w adjacent to v:
			// to calculate the new smaller distance, Min{weight(v,w), w.dist}
			while (itr.hasNext()) {
				Vertex<T> w = itr.next();
				// update path of w
				if (unknownVs.contains(w)) {
					Double vwWeight = getWeightOf2V2(v, w);
					if (vwWeight == null) {
						continue;
					}
					double wDist = distMap.get(w);
					if (vwWeight < wDist) {
						distMap.put(w, vwWeight);
						pathMap.put(w, v);
					}
				}
			}
		}

		return mstEdges;
	}

	/**
	 * the Kruskal's algorithm implement of minimum spanning tree.
	 * 
	 * @return all edges in the minimum spanning tree
	 */
	public ArrayList<Edge<T>> mstOfKruskal() {
		// the minimum spanning tree's edges
		ArrayList<Edge<T>> mstEdges = new ArrayList<Edge<T>>();
		DisjSets<Vertex<T>> ds = new DisjSets<Vertex<T>>(getVertexCount());
		PriorityQueue<Edge<T>> pg = new PriorityQueue<Edge<T>>(getEdges());
		Edge<T> e;
		Vertex<T> u, v;

		while (mstEdges.size() < getVertexCount() - 1 && !pg.isEmpty()) {
			e = pg.remove();
			// Edge e = (u, v)
			u = e.getSrcVertex();
			v = e.getDestVertex();
			Vertex<T> uSet = ds.find(u);
			Vertex<T> vSet = ds.find(v);
			if (uSet != vSet) {
				// Accept the edge
				mstEdges.add(e);
				ds.union(uSet, vSet);
			}// else ring occurs
		}

		return mstEdges;
	}

	/**
	 * to find all paths from src to dest
	 * 
	 * @param src
	 * @param dest
	 */
	public void findPath(Vertex<T> src, Vertex<T> dest) {
		// to mark visited vertexes in DFS(Depth-First Search).
		Set<Vertex<T>> visitedVs = new HashSet<Vertex<T>>();
		// the intermediate vertexes in the path from src to dest.
		Set<Vertex<T>> goByVs = new HashSet<Vertex<T>>();
		// the cross-roads: there may be one more branches from one vertex.
		Map<Vertex<T>, Set<Vertex<T>>> v2vPaths = new HashMap<Vertex<T>, Set<Vertex<T>>>();

		// to run Depth-First Search method
		dfsOfFindPath(src, dest, visitedVs, goByVs, v2vPaths);

		System.out.println(src + "->" + dest + ": " + goByVs);
		System.out.println("Paths go as:" + v2vPaths);
	}

	/**
	 * Depth-First Search method: is set to search paths destination of dest
	 * 
	 * @param src
	 * @param dest
	 * @param visited
	 * @param goByVs
	 * @param v2vPaths
	 * @return
	 */
	private boolean dfsOfFindPath(Vertex<T> src, Vertex<T> dest, Set<Vertex<T>> visited, Set<Vertex<T>> goByVs,
			Map<Vertex<T>, Set<Vertex<T>>> v2vPaths) {
		visited.add(src); // visit(v); mark src has been done
		if (src == dest) {
			return true;
		}
		LinkedList<Vertex<T>> toVs = src.getToNighbors();
		Iterator<Vertex<T>> itr = toVs.iterator();
		// for each Vertex w adjacent to v: visit(w);
		boolean flag = false;
		while (itr.hasNext()) {
			Vertex<T> w = itr.next();
			if (!visited.contains(w)) {
				// dfs(w): w is a newer
				boolean tt = dfsOfFindPath(w, dest, visited, goByVs, v2vPaths);
				if (tt) {
					// there is a way to dest
					goByVs.add(w);
					if (v2vPaths.get(src) != null) {
						v2vPaths.get(src).add(w);
					} else {
						Set<Vertex<T>> crossroads = new HashSet<Vertex<T>>();
						crossroads.add(w);
						v2vPaths.put(src, crossroads);
					}
					flag = true;
				}
			} else {
				// w is already tackled over dfs
				if (goByVs.contains(w)) {
					// there is a way to dest
					goByVs.add(src);
					if (v2vPaths.get(src) != null) {
						v2vPaths.get(src).add(w);
					} else {
						Set<Vertex<T>> crossroads = new HashSet<Vertex<T>>();
						crossroads.add(w);
						v2vPaths.put(src, crossroads);
					}
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * calculate the max flow from s to t
	 * 
	 * @param s
	 * @param t
	 * @return
	 */
	public double maxFlow(Vertex<T> s, Vertex<T> t) {
		double masFlow = 0;
		// init f[u,v] and f[v,u] for each edge
		for (int i = 0; i < this.edges.size(); i++) {
			Edge<T> e = edges.get(i);
			e.forwardFlow = 0.0;
			// if there isn't backward edge, use e.backwardFlow to simulate f[v,u]
			// otherwise, be.forwardFlow is ok
			Edge<T> be = this.findEdgeOf2V(e.getDestVertex(), e.getSrcVertex());
			if (be == null) {
				e.backwardFlow = 0.0;
			}
		}

		Queue<Vertex<T>> q = new ArrayDeque<Vertex<T>>();
		// to find all argument path p from s to t, and calculate the flow
		while (true) {
			// here, we use BFS to search one path s->t
			q.clear();
			for (Vertex<T> v : this.vertexes) {
				v.dist = Integer.MAX_VALUE;
				v.path = null;
			}
			s.dist = 0;
			q.add(s);
			boolean found = false;
			while (!q.isEmpty()) {
				Vertex<T> v = q.remove();
				LinkedList<Vertex<T>> adjVs = v.getAllNighbors();
				Iterator<Vertex<T>> itr = adjVs.iterator();
				while (itr.hasNext()) {
					Vertex<T> w = itr.next();
					// get the residual flow of edge v->w
					double residualFlow = getFlowOfEdge(v, w);
					if (w.dist == Integer.MAX_VALUE && residualFlow > 0) {
						w.dist = v.dist + 1;
						w.path = v;
						if (w == t) {
							found = true;
							break;// if the path is found, ignore the later procedure
						}
						q.add(w);
					}
				}
				if (found) {
					break;// if the path is found, ignore the later procedure
				}
			}
			if (t.dist != Integer.MAX_VALUE) {
				// now, we find a path from s to t, then calculate Cf(p)
				double cfp = getFlowOfPath(s, t);
				System.out.println(cfp);
				printPath(t, s);
				masFlow += cfp;
				// and calculate all the f[u,v] and f[v,u] in the path
				Vertex<T> cur = t;
				Vertex<T> pre = cur.path; // previous vertex
				while (pre != null && cur != s) {
					// to subtract the cft from the edge on this path
					updateFlowOfEdge(pre, cur, cfp);
					cur = pre;
					pre = cur.path;
				}
			} else {
				// all path (within limitation of residual flow) is found out
				break;
			}
			// to find another path from s to t
		}

		return masFlow;
	}

	/**
	 * get the Residual Flow of the edge between u and v
	 * <dl>
	 * <dt>Residual capacity:</dt>
	 * <dd>Cf(u,v) = c(u,v) - f(u,v)</dd>
	 * <dd>c(u,v): the weidgt of edge beween u and v</dd>
	 * <dd>f(u,v): the flow which is already allocated</dd>
	 * </dl>
	 * <p>
	 * NOTE: if there isn't a edge from u to v, use backward edge(v,u) to simulate, of course, the direction is all
	 * opposed
	 * </p>
	 * 
	 * @param u
	 * @param v
	 * @return Cf(u,v)
	 */
	private double getFlowOfEdge(Vertex<T> u, Vertex<T> v) {
		double residualFlow = 0;
		Edge<T> e = findEdgeOf2V(u, v);
		if (e != null) {
			residualFlow = e.getWeight() - e.forwardFlow;
		} else {
			// use edge(v,u).backwardFlow to simulate
			Edge<T> be = this.findEdgeOf2V(v, u);
			if (be == null) {
				// that's impossible
			}
			residualFlow = -be.backwardFlow;
		}
		return residualFlow;
	}

	/**
	 * get the flow of one argument path:
	 * <dl>
	 * <dd>Cf(p) = min {Cf(u,v):(u,v)in the path p}</dd>
	 * </dl>
	 * 
	 * @param u
	 * @param v
	 * @return Cf(p)
	 */
	private double getFlowOfPath(Vertex<T> u, Vertex<T> v) {
		double cfp = 0;
		boolean first = true;
		Vertex<T> cur = v;
		Vertex<T> pre = cur.path; // previous vertex
		while (pre != null && cur != u) {
			double temp = getFlowOfEdge(pre, cur);
			if (first) {
				cfp = temp;
				first = false;
			} else {
				cfp = Math.min(cfp, temp);
			}
			cur = pre;
			pre = cur.path;
		}
		return cfp;
	}

	/**
	 * update Flow of the edge between u and v
	 * <p>
	 * NOTE: if there isn't a edge from u to v, use backward edge(v,u) to simulate, of course, the direction is all
	 * opposed
	 * </p>
	 * 
	 * @param u
	 * @param v
	 */
	private void updateFlowOfEdge(Vertex<T> u, Vertex<T> v, double flowOfPath) {
		Edge<T> e = findEdgeOf2V(u, v);
		if (e != null) {
			e.forwardFlow += flowOfPath;
			// if there isn't backward edge, use e.backwardFlow to simulate f[v,u]
			// otherwise, be.forwardFlow is ok
			Edge<T> be = this.findEdgeOf2V(e.getDestVertex(), e.getSrcVertex());
			if (be == null) {
				e.backwardFlow = -e.forwardFlow;
			} else {
				be.forwardFlow -= flowOfPath;
			}
		} else {
			Edge<T> e2 = this.findEdgeOf2V(v, u);
			e2.forwardFlow -= flowOfPath;
			e2.backwardFlow = -e2.forwardFlow;
		}
	}

	/**
	 * single source shortest path
	 * <p>
	 * this method is fit to the un-weighted graph, and the result can be shown by the method
	 * {@link #printPath(Vertex, Vertex)}
	 * </p>
	 * 
	 * @param start
	 */
	public void ssspOfUnweighted(Vertex<T> start) {
		// init
		for (Vertex<T> v : this.vertexes) {
			v.dist = Integer.MAX_VALUE;
			v.path = null;
		}
		start.dist = 0;
		// use BFS
		Queue<Vertex<T>> q = new ArrayDeque<Vertex<T>>();
		q.add(start);
		while (!q.isEmpty()) {
			Vertex<T> v = q.remove();
			LinkedList<Vertex<T>> adjVs = v.getToNighbors();
			Iterator<Vertex<T>> itr = adjVs.iterator();
			while (itr.hasNext()) {
				Vertex<T> w = itr.next();
				if (w.dist == Integer.MAX_VALUE) {
					w.dist = v.dist + 1;
					w.path = v;
					q.add(w);
				}
			}
		}
	}

	/**
	 * the core method for showing the path from src to dest
	 * 
	 * @param dest
	 * @param src
	 */
	private void printPathInner(Vertex<T> dest, Vertex<T> src) {
		if (dest.path != null && dest != src) {
			printPathInner(dest.path, src);
			System.out.print(" -> ");
		}
		System.out.print(dest);
	}

	/**
	 * the method is called to show the path from src to dest
	 * <p>
	 * it's due to {@link #ssspOfUnweighted(Vertex)} or other method on searching path
	 * </p>
	 * 
	 * @param dest
	 * @param src
	 */
	public void printPath(Vertex<T> dest, Vertex<T> src) {
		printPathInner(dest, src);
		System.out.println();
	}

	/**
	 * 
	 * @param dest
	 */
	public void printPath(Vertex<T> dest) {
		printPathInner(dest, null);
		System.out.println();
	}
}
