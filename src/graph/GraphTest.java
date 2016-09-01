package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class GraphTest {
	static double genRandomWeight() {
		return Math.rint(Math.random() * 15);
	}

	public static void main(String[] args) {
		test3();
	}
	
	static void test3() {
		Graph<String> graph = new Graph<String>(GraphKind.DIRECTED);
		graph.addEdge("s", "v1", 16D);
		graph.addEdge("s", "v2", 13D);
		graph.addEdge("v1", "v2", 10D);
		graph.addEdge("v1", "v3", 12D);
		graph.addEdge("v2", "v1", 4D);
		graph.addEdge("v2", "v4", 14D);
		graph.addEdge("v3", "v2", 9D);
		graph.addEdge("v3", "t", 20D);
		graph.addEdge("v4", "v3", 7D);
		graph.addEdge("v4", "t", 4D);
		ArrayList<Vertex<String>> vertices = graph.getVertexes();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex<String> v = vertices.get(i);
			System.out.println(v.toString2() + ". out-degree:" + v.getOutDegree() + ", in-degree:" + v.getInDegree());
		}

		ArrayList<Edge<String>> edges = graph.getEdges();
		for (int i = 0; i < edges.size(); i++) {
			Edge<String> e = edges.get(i);
			System.out.println(e);
		}

		Vertex<String> s = graph.getVertexByData("s");
		Vertex<String> t = graph.getVertexByData("t");

		double maxFlow = graph.maxFlow(s, t);
		System.out.println(maxFlow);
	}

	static void test2() {
		Graph<String> graph = new Graph<String>(GraphKind.DIRECTED);
		graph.addEdge("s", "a", 3D);
		graph.addEdge("s", "b", 2D);
		graph.addEdge("a", "b", 1D);
		graph.addEdge("a", "c", 3D);
		graph.addEdge("a", "d", 4D);
		graph.addEdge("b", "d", 2D);
		graph.addEdge("c", "t", 2D);
		graph.addEdge("d", "t", 3D);
		ArrayList<Vertex<String>> vertices = graph.getVertexes();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex<String> v = vertices.get(i);
			System.out.println(v.toString2() + ". out-degree:" + v.getOutDegree() + ", in-degree:" + v.getInDegree());
		}

		ArrayList<Edge<String>> edges = graph.getEdges();
		for (int i = 0; i < edges.size(); i++) {
			Edge<String> e = edges.get(i);
			System.out.println(e);
		}

		Vertex<String> s = graph.getVertexByData("s");
		Vertex<String> t = graph.getVertexByData("t");

//		graph.ssspOfUnweighted(s);
//		graph.printPath(t);

		double maxFlow = graph.maxFlow(s, t);
		System.out.println(maxFlow);
	}

	static void test1() {
		Graph<String> graph = new Graph<String>(GraphKind.DIRECTED);
		Vertex<String>[] vertexes = new Vertex[7];
		vertexes[0] = new Vertex<String>("1");
		vertexes[1] = new Vertex<String>("2");
		vertexes[2] = new Vertex<String>("3");
		vertexes[3] = new Vertex<String>("4");
		vertexes[4] = new Vertex<String>("5");
		vertexes[5] = new Vertex<String>("6");
		vertexes[6] = new Vertex<String>("7");
		for (int i = 0; i < vertexes.length; i++) {
			graph.addVertex(vertexes[i]);
		}
		// 1 -> 2
		graph.addEdge(vertexes[0], vertexes[1], 2d);
		// 1 -> 3
		// graph.addEdge(vertexes[0], vertexes[2], 4d);
		// 1 -> 4
		graph.addEdge(vertexes[0], vertexes[3], 1d);
		// 2 -> 4
		graph.addEdge(vertexes[1], vertexes[3], 3d);
		// 2 -> 5
		graph.addEdge(vertexes[1], vertexes[4], 10d);
		// 3 -> 1
		graph.addEdge(vertexes[2], vertexes[0], 4d);
		// 3 -> 6
		graph.addEdge(vertexes[2], vertexes[5], 5d);
		// 4 -> 3
		graph.addEdge(vertexes[3], vertexes[2], 2d);
		// 4 -> 6
		graph.addEdge(vertexes[3], vertexes[5], 8d);
		// 4 -> 7
		graph.addEdge(vertexes[3], vertexes[6], 4d);
		// 4 -> 5
		graph.addEdge(vertexes[3], vertexes[4], 2d);
		// 5 -> 4
		// graph.addEdge(vertexes[4], vertexes[3], 7d);
		// 5 -> 7
		graph.addEdge(vertexes[4], vertexes[6], 6d);
		// 7 -> 6
		graph.addEdge(vertexes[6], vertexes[5], 1d);

		ArrayList<Vertex<String>> vertices = graph.getVertexes();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex<String> v = vertices.get(i);
			System.out.println(v.toString2() + ". out-degree:" + v.getOutDegree() + ", in-degree:" + v.getInDegree());
		}

		ArrayList<Edge<String>> edges = graph.getEdges();
		for (int i = 0; i < edges.size(); i++) {
			Edge<String> e = edges.get(i);
			System.out.println(e);
		}

		try {
			ArrayList<Vertex<String>> topSortArray = graph.topSort();
			System.out.println(topSortArray);
		} catch (CycleFoundException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < vertices.size(); i++) {
			System.out.println(vertices.get(i));
			Map[] result = graph.sssp(vertices.get(i));
			graph.printPath(vertices.get(i), result[0], result[1]);
		}

		System.out.println("Minimum spanning tree: Prim");
		ArrayList<Edge<String>> mstEdges = graph.mstOfPrim();
		System.out.println(mstEdges);

		System.out.println("Minimum spanning tree: Kruskal");
		ArrayList<Edge<String>> mstEdges2 = graph.mstOfKruskal();
		System.out.println(mstEdges2);

		System.out.println("Compare result:");
		Collections.sort(mstEdges);
		Collections.sort(mstEdges2);
		System.out.println(mstEdges.equals(mstEdges2));

		for (int i = 1; i < vertexes.length; i++) {
			graph.findPath(vertexes[0], vertexes[i]);
		}

	}

}
