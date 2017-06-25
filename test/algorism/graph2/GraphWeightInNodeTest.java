package algorism.graph2;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import algorism.graph2.GraphWeightInNode.Path;

public class GraphWeightInNodeTest {

	@Test
	public void testFindZeroInDegreeNode() throws IllegalAccessException {
		GraphWeightInNode graph = new GraphWeightInNode();
		graph.addNode("A", 1).addNode("B", 2).addNode("C", 2);
		graph.addLink("A", "B").addLink("B", "C").addLink("A", "C");
		String node = graph.findZeroInDegreeNode();
		assertEquals(node, "A");
	}

	@Test
	public void testFindAllPaths() throws IllegalAccessException {
		GraphWeightInNode graph = new GraphWeightInNode();
		graph.addNode("A", 1).addNode("B", 2).addNode("C", 2);
		graph.addLink("A", "B").addLink("B", "C").addLink("A", "C");
		Collection<Path> paths = graph.findAllPaths();
		System.out.println(paths);
	}

	@Test(timeout = 5000)
	public void testFindAllPaths2() throws IllegalAccessException {
		GraphWeightInNode graph = new GraphWeightInNode();
		graph.addNode("A", 1).addNode("B", 2).addNode("C", 2).addNode("D", 2)
		        .addNode("E", 2);
		graph.addLink("A", "B").addLink("B", "C").addLink("A", "C")
		        .addLink("A", "D").addLink("B", "D").addLink("D", "E")
		        .addLink("D", "C");
		graph.addLink("E", "B"); // 成环了
		Collection<Path> paths = graph.findAllPaths();
		System.out.println(paths);
	}

	@Test
	public void testFindMaxWeightPath() throws IllegalAccessException {
		GraphWeightInNode graph = new GraphWeightInNode();
		graph.addNode("A", 1).addNode("B", 2).addNode("C", 2);
		graph.addLink("A", "B").addLink("B", "C").addLink("A", "C");
		Path p = graph.findMaxWeightPath();
		assertEquals(p.weight, 5);
	}

	@Test
	public void testGetMaxWeightPathWeight() throws IllegalAccessException {
		GraphWeightInNode graph = new GraphWeightInNode();
		graph.addNode("A", 1).addNode("B", 2).addNode("C", 2);
		graph.addLink("A", "B").addLink("B", "C").addLink("A", "C");
		int w = graph.getMaxWeightPathWeight();
		assertEquals(w, 5);
	}

}
