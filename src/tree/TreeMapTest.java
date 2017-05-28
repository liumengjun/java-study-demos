package tree;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;

public class TreeMapTest {
	private static Field rootField;
	private static Class treeMapEntry;
	private static Map<String, Field> entryFieldMap;

	public static void main(String[] args) throws Exception {
		reflectTreeMap();

		TreeMap treeMap = new TreeMap();
		// do test
//		for (int key = 1, value = 'a'; value <= 'z'; value++, key++) {
//			treeMap.put(key, (char) value);
//		}
		Random rand = new Random();
		for (int i = 1, c = 'a'; c <= 'z'; c++, i++) {
			int key = rand.nextInt('z'-'0');
			char value = (char)(key+'0');
			treeMap.put(key, value);
		}
		Object v = treeMap.get(3);
		System.out.println(v);
		System.out.println(treeMap);
		
		System.out.println("size: " + treeMap.size());
		System.out.println(stringTreeMapInLevel(treeMap));
		System.out.println(stringTreeMap(treeMap));
	}

	static void reflectTreeMap() {
		try {
			rootField = TreeMap.class.getDeclaredField("root");
			rootField.setAccessible(true);
			treeMapEntry = Class.forName("java.util.TreeMap$Entry");
			Field[] entryFields = treeMapEntry.getDeclaredFields();
			entryFieldMap = new HashMap<String, Field>(entryFields.length * 2);
			for (Field f : entryFields) {
				f.setAccessible(true);
				entryFieldMap.put(f.getName(), f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	interface VisitInterface {
		void visit(Object node);
	}
	
	static String stringTreeMap(TreeMap treeMap) throws Exception {
		if (rootField==null) {
			reflectTreeMap();
		}
		Object root = rootField.get(treeMap);
		if (root == null) {
			return "{}";
		}
		// init
		preOrder(treeMap, new VisitInterface() {
			public void visit(Object node) {
				if (node != null) {
					visited(node, false);
				}
			}
		});
		final StringBuffer visitBuffer = new StringBuffer();
		final LinkedList<Object> pList = new LinkedList<Object>();
		VisitInterface visitor = new VisitInterface() {
			public void visit(Object node) {
				if (node == null) {
					visitBuffer.append('\n');
					return;
				}
				Object p = parent(node);
				while (p != null) {
					pList.add(p);
					p = parent(p);
				}
				while (!pList.isEmpty()) {
					p = pList.pollLast();
					if (p == parent(node)) {
						if (left(p) == node && right(p) != null) {
							// left is earlier then right
							visitBuffer.append("├───");
						} else {
							visitBuffer.append("└───");
						}
					} else {
						if ((left(p) == null || left(p) != null && visited(left(p)))
								&& (right(p) == null || right(p) != null && visited(right(p)))) {
							visitBuffer.append("    ");
						} else {
							visitBuffer.append("│   ");
						}
					}
				}
				visitBuffer.append("{" + data(node) + "}\n");
				visited(node, true);
			}
		};
		preOrder(treeMap, visitor);
		return visitBuffer.toString();
	}
	private static Map<Object, Boolean> visitedMap = new HashMap<Object, Boolean>();
	private static boolean visited(Object node) {
		if (node == null) {
			return true;
		}
		Boolean b = visitedMap.get(node);
		if (b==null) {
			return false;
		}
		return b;
	}
	private static void visited(Object node, boolean v) {
		visitedMap.put(node, v);
	}
	static void preOrder(TreeMap treeMap, VisitInterface visitor) throws Exception {
		if (rootField==null) {
			reflectTreeMap();
		}
		Object root = rootField.get(treeMap);
		preOrder(root, visitor);
	}
	private static void preOrder(Object node, VisitInterface visitor) {
		if (node != null) {
			if (visitor != null) {
				visitor.visit(node);
			} else {
				System.out.println("{"+data(node)+"}");
			}
			preOrder(left(node), visitor);
			preOrder(right(node), visitor);
		}
	}
	
	static String stringTreeMapInLevel(TreeMap treeMap) throws Exception {
		if (rootField==null) {
			reflectTreeMap();
		}
		Object root = rootField.get(treeMap);
		if (root == null) {
			return "{}";
		}
		final StringBuffer visitBuffer = new StringBuffer();
		final class _nodePos {
			int value;
		}
		final _nodePos _levelPos = new _nodePos();
		VisitInterface visitor = new VisitInterface() {
			public void visit(Object node) {
				if (node == null) {
					visitBuffer.append('\n');
					_levelPos.value = 0;
					return;
				}
				if (left(node) != null) {
					int llw = widthLeft(left(node)) + 1;
					int lw = widthLeft(node) + 1;
					int i = 0;
					for (; i < llw; i++) {
						visitBuffer.append("    ");
						_levelPos.value++;
					}
					visitBuffer.append("┌────");
					_levelPos.value++;
					for (i++; i < lw; i++) {
						visitBuffer.append("────");
						_levelPos.value++;
					}
				}
				if (parent(node) != null) {
					Object p = parent(node);
					int pPos = levelPos(p);
					pPos += (node == left(p)) ? (-widthLeft(p) - 1) : (widthLeft(node) + 1);
					while (_levelPos.value < pPos) {
						visitBuffer.append((left(node) != null) ? "────" : "      ");
						_levelPos.value++;
					}
				}
				visitBuffer.append("{"+data(node)+"}");
				levelPos(node, _levelPos.value);
				_levelPos.value++;
				if (right(node) != null) {
					int rlw = widthLeft(right(node)) + 1;
					for (int i = 1; i < rlw; i++) {
						visitBuffer.append("────");
						_levelPos.value++;
					}
					visitBuffer.append("───┐");
					_levelPos.value++;
				}
				if (parent(node) != null) {
					int pPos = levelPos(parent(node));
					while (_levelPos.value < pPos) {
						visitBuffer.append("    ");
						_levelPos.value++;
					}
				}
			}
		};
		levelOrder2(treeMap, visitor);
		return visitBuffer.toString();
	}
	static void levelOrder2(TreeMap treeMap, VisitInterface visitor) throws Exception {
		if (rootField==null) {
			reflectTreeMap();
		}
		Object root = rootField.get(treeMap);
		if (root == null) {
			return;
		}
//		int levelNum = 0;
//		int levelLoc = 0;
		Queue<Object> queue = new ArrayDeque<Object>(treeMap.size());
//		root.levelNum = levelNum;
//		root.levelLoc = 0;
		queue.add(root);
		queue.add(new Object());// end label of current level
//		levelNum++;
		while (!queue.isEmpty()) {
			Object curNode = queue.poll();
			Object following = queue.peek();
			if (visitor != null) {
				visitor.visit(curNode);
			} else {
				System.out.println("{"+data(curNode)+"}");
			}
			if (left(curNode) != null) {
//				curNode.leftChild.levelNum = levelNum;
//				curNode.leftChild.levelLoc = levelLoc++;
				queue.add(left(curNode));
			}
			if (right(curNode) != null) {
//				curNode.rightChild.levelNum = levelNum;
//				curNode.rightChild.levelLoc = levelLoc++;
				queue.add(right(curNode));
			}
			if (data(following) == null) {
				queue.poll();// just remove the end label of previous level
				if (queue.peek() == null) {
					// no more data, break
					break;
				}
				queue.add(new Object());// end label of current level
//				levelNum++;
//				levelLoc = 0;
				if (visitor != null) {
					visitor.visit(null);
				} else {
					System.out.println();
				}
			}
		}
	}
	private static Map<Object, Integer> levelPosMap = new HashMap<Object, Integer>();
	private static int levelPos(Object node) {
		if (node == null) {
			return 0;
		}
		Integer l = levelPosMap.get(node);
		if (l==null) {
			return 0;
		}
		return l;
	}
	private static void levelPos(Object node, int v) {
		levelPosMap.put(node, v);
	}
	
	private static int widthLeft(Object node) {
		if (node == null || left(node) == null) {
			return 0;
		}
		int wL = widthLeft(left(node)) + 1;
		if (right(left(node)) != null) {
			int wLR = widthLeft(right(left(node)));
			wL = wL > wLR ? wL : wLR;
			wL += widthRight(right(left(node))) + 1;
		}
		return wL;
	}
	
	private static int widthRight(Object node) {
		// the width of right's left side, this node do not count
		if (node == null || right(node) == null) {
			return 0;
		}
		int wR = widthRight(right(node)) + 1;
		if (left(right(node)) != null) {
			int wRL = widthRight(left(right(node)));
			wR = wR > wRL ? wR : wRL;
			wR += widthLeft(left(right(node)));
		}
		return wR;
	}
	
	private static Object parent(Object obj) {
		try {
			return entryFieldMap.get("parent").get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object left(Object obj) {
		try {
			return entryFieldMap.get("left").get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object right(Object obj) {
		try {
			return entryFieldMap.get("right").get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object key(Object obj) {
		try {
			return entryFieldMap.get("key").get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object value(Object obj) {
		try {
			return entryFieldMap.get("value").get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static boolean color(Object obj) {
		try {
			return (boolean)entryFieldMap.get("color").get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static String data(Object obj) {
		if (!treeMapEntry.isInstance(obj) ) {
			return null;
		}
		StringBuffer d = new StringBuffer();
		d.append(key(obj)).append('=').append(value(obj)).append(',').append(color(obj)?'R':'B');
		return d.toString();
	}
}
