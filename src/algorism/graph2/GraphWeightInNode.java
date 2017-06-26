package algorism.graph2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GraphWeightInNode {

	/* node map <name, weight> */
	Map<String, Integer> nodes = new HashMap<>();
	/* links <from-node, <to-nodes set> */
	Map<String, Set<String>> links = new HashMap<>();

	/*
	 * 一串连续的link上的节点组成路径
	 */
	class Path {
		List<String> pathNodes = new ArrayList<String>();
		int weight;
		boolean closeCycle;

		Path addNode(String node) {
			pathNodes.add(node);
			weight += GraphWeightInNode.this.nodes.get(node);
			return this;
		}

		Path copy() {
			Path that = new Path();
			that.pathNodes.addAll(this.pathNodes);
			that.weight = this.weight;
			that.closeCycle = this.closeCycle;
			return that;
		}

		String getFirstNode() {
			return (pathNodes.isEmpty()) ? null : pathNodes.get(0);
		}

		String getLastNode() {
			int l = this.pathNodes.size();
			return (l > 0) ? pathNodes.get(l - 1) : null;
		}

		public String toString() {
			return "{" + this.weight + ": " + this.pathNodes.toString() + "}";
		}
	}

	/**
	 * 添加节点`nodeName`到该graph
	 * 
	 * @param nodeName
	 * @param nodeWeight
	 * @return 该graph自身
	 */
	public GraphWeightInNode addNode(String nodeName, int nodeWeight) {
		nodes.put(nodeName, nodeWeight);
		return this;
	}

	/**
	 * 添加路径`fromNode -> toNode`到该graph，返回
	 * 
	 * @param fromNode
	 * @param toNode
	 * @return 该graph自身
	 * @throws IllegalAccessException
	 */
	public GraphWeightInNode addLink(String fromNode, String toNode) throws IllegalAccessException {
		if (!nodes.containsKey(fromNode) || !nodes.containsKey(toNode)) {
			throw new IllegalAccessException("请先添加node");
		}
		Set<String> toNodesSet = links.get(fromNode);
		if (toNodesSet == null) {
			toNodesSet = new HashSet<>();
			links.put(fromNode, toNodesSet);
		}
		toNodesSet.add(toNode);
		return this;
	}

	/**
	 * 查到入度为0的节点，即没有路径指向该节点
	 * 
	 * @return
	 */
	public String findZeroInDegreeNode() {
		Set<String> keys = new HashSet<>(nodes.keySet());
		for (String node : keys) {
			boolean hasLinkToNode = false;
			for (Set<String> toNodes : links.values()) {
				if (toNodes.contains(node)) {
					hasLinkToNode = true;
					break;
				}
			}
			if (!hasLinkToNode) {
				return node;
			}
		}
		return null;
	}

	/**
	 * 获取所有路径
	 * 
	 * @return
	 */
	public Collection<Path> findAllPaths() {
		if (nodes.isEmpty()) {
			return null;
		}
		Queue<Path> paths = new ArrayDeque<>();
		// 确定起始节点，根据startNode初始化 （或许应该把所有长度为一的简单路径最为初始化数据，此处假设没有环）
		String startNode = this.findZeroInDegreeNode();
		if (startNode == null) {
			startNode = nodes.keySet().iterator().next();
		}
		for (String toNode : links.get(startNode)) {
			Path p = new Path().addNode(startNode).addNode(toNode);
			paths.add(p);
		}
		// 逐步向下走
		while (true) {
			Queue<Path> morePaths = new ArrayDeque<>();
			boolean hasFurtherLink = false;
			for (Path p : paths) {
				if (p.closeCycle) {
					// 封闭环，终结该路径遍历，仍然保留该路径
					morePaths.add(p);
				}
				// 获取已知路径的最后一个节点
				String lastNode = p.getLastNode();
				int nPos = p.pathNodes.indexOf(lastNode);
				boolean hasCycle = (nPos != -1 && nPos != p.pathNodes.size() - 1);
				Set<String> toNodes = links.get(lastNode);
				if (toNodes != null && !toNodes.isEmpty()) {
					boolean hasNextNode = false;
					// 添加后续节点到新的路径
					for (String toNode : toNodes) {
						int idxToNode = p.pathNodes.indexOf(toNode);
						if (hasCycle && idxToNode > nPos) {
							// 环内节点，不处理
						} else {
							Path furtherPath = p.copy().addNode(toNode);
							morePaths.add(furtherPath);
							hasFurtherLink = true;
							hasNextNode = true;
						}
					}
					if (hasCycle && !hasNextNode) {
						// 检测到封闭环，更新该路径并保存
						p.closeCycle = true;
						morePaths.add(p);
					}
				} else {
					// 仍然保留该路径
					morePaths.add(p);
				}
			}
			paths = morePaths;
			if (!hasFurtherLink) {
				break;
			}
		}
		return paths;
	}

	/**
	 * 查找权重最大的路径
	 * 
	 * @return
	 */
	public Path findMaxWeightPath() {
		Collection<Path> paths = this.findAllPaths();
		Path maxWeightPath = null;
		int maxWeight = 0;
		for (Path p : paths) {
			if (maxWeightPath == null || p.weight > maxWeight) {
				maxWeightPath = p;
				maxWeight = p.weight;
			}
		}
		return maxWeightPath;
	}

	/**
	 * 获取权重最大的路径的权重
	 * 
	 * @return
	 */
	public int getMaxWeightPathWeight() {
		Path p = this.findMaxWeightPath();
		return (p != null) ? p.weight : -1;
	}
}
