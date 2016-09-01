package tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BinaryTree<T> {

	BinaryNode<T> root;

	/**
	 * 
	 * @param data
	 */
	public BinaryNode<T> insert(T data) {
		if (this.root == null) {
			this.root = new BinaryNode<T>(data);
			return this.root;
		}

		BinaryNode<T> curNode = this.root;
		while (curNode != null) {
			if (curNode.leftChild == null) {
				curNode.leftChild = new BinaryNode<T>(data, curNode);
				return curNode.leftChild;
			}
			if (curNode.rightChild == null) {
				curNode.rightChild = new BinaryNode<T>(data, curNode);
				return curNode.rightChild;
			}
			if (curNode.leftChild.weight() > curNode.rightChild.weight()) {
				curNode = curNode.rightChild;
			} else {
				curNode = curNode.leftChild;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param data
	 */
	public boolean delete(T data) {
		BinaryNode<T> target = search(data);

		if (target == null) {
			return false;
		}
		if (target == this.root && this.root.isLeaf()) {
			this.root = null;
			return true;
		}
		// fake delete, to get last data and assign it to target, then delete
		// last node
		BinaryNode<T> curNode = target;
		while (curNode != null) {
			if (curNode.leftChild == null && curNode.rightChild == null) {
				// assign its data to target
				target.data = curNode.data;
				// delete last node
				BinaryNode<T> parent = curNode.parent;
				if (parent.leftChild == curNode) {
					parent.leftChild = null;
				} else {
					parent.rightChild = null;
				}
				break;
			}
			if (curNode.rightChild == null) {
				curNode = curNode.leftChild;
				continue;
			}
			if (curNode.leftChild == null) {
				curNode = curNode.rightChild;
				continue;
			}
			if (curNode.leftChild.height() > curNode.rightChild.height()) {
				curNode = curNode.leftChild;
			} else {
				curNode = curNode.rightChild;
			}
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public int height() {
		return BinaryNode.height(this.root);
	}

	/**
	 * 
	 * @return
	 */
	int weight() {
		return BinaryNode.weight(this.root);
	}

	int width() {
		return BinaryNode.width(this.root);
	}

	int widthLeft() {
		return BinaryNode.widthLeft(this.root);
	}

	int widthRight() {
		return BinaryNode.widthRight(this.root);
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return weight();
	}

	/**
	 * 
	 * @return
	 */
	boolean isEmpty() {
		return (this.root == null);
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public BinaryNode<T> search(T target) {
		return search(target, this.root);
	}

	private BinaryNode<T> search(T target, BinaryNode<T> node) {
		if (node != null) {
			// check self
			if (node.data == target || node.data.equals(target)) {
				return node;
			}
			BinaryNode<T> temp = null;
			// check left child
			temp = search(target, node.leftChild);
			if (temp != null) {
				return temp;
			}
			// check right child
			temp = search(target, node.rightChild);
			if (temp != null) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	boolean contains(T target) {
		return (search(target) != null);
	}

	/**
	 * for traverse: just print
	 * 
	 * @param node
	 */
	private void visit(BinaryNode<T> node) {
		if (node == null) {
			System.out.println();
			return;
		}
		System.out.print("{" + node.data + "}");
	}

	/**
	 * for traverse
	 * 
	 * @author
	 * 
	 * @param <T>
	 */
	interface VisitInterface<T> {
		void visit(BinaryNode<T> node);
	}

	/**
	 * 
	 */
	void preOrder() {
		preOrder(this.root, null);
	}

	/**
	 * 
	 * @param visitor
	 */
	void preOrder(VisitInterface<T> visitor) {
		preOrder(this.root, visitor);
	}

	private void preOrder(BinaryNode<T> node, VisitInterface<T> visitor) {
		if (node != null) {
			if (visitor != null) {
				visitor.visit(node);
			} else {
				visit(node);
			}
			preOrder(node.leftChild, visitor);
			preOrder(node.rightChild, visitor);
		}
	}

	/**
	 * 
	 */
	void middleOrder() {
		middleOrder(this.root, null);
	}

	/**
	 * 
	 * @param visitor
	 */
	void middleOrder(VisitInterface<T> visitor) {
		middleOrder(this.root, visitor);
	}

	private void middleOrder(BinaryNode<T> node, VisitInterface<T> visitor) {
		if (node != null) {
			middleOrder(node.leftChild, visitor);
			if (visitor != null) {
				visitor.visit(node);
			} else {
				visit(node);
			}
			middleOrder(node.rightChild, visitor);
		}
	}

	/**
	 * 
	 */
	void postOrder() {
		postOrder(this.root, null);
	}

	/**
	 * 
	 * @param visitor
	 */
	void postOrder(VisitInterface<T> visitor) {
		postOrder(this.root, visitor);
	}

	private void postOrder(BinaryNode<T> node, VisitInterface<T> visitor) {
		if (node != null) {
			postOrder(node.leftChild, visitor);
			postOrder(node.rightChild, visitor);
			if (visitor != null) {
				visitor.visit(node);
			} else {
				visit(node);
			}
		}
	}

	/**
	 * 
	 */
	void levelOrder() {
		levelOrder(null);
	}

	/**
	 * 
	 * @param visitor
	 */
	void levelOrder(VisitInterface<T> visitor) {
		if (this.root == null) {
			return;
		}
		Queue<BinaryNode<T>> queue = new ArrayDeque<BinaryNode<T>>();
		queue.add(this.root);

		while (!queue.isEmpty()) {
			BinaryNode<T> curNode = queue.poll();
			if (visitor != null) {
				visitor.visit(curNode);
			} else {
				visit(curNode);
			}
			if (curNode.leftChild != null) {
				queue.add(curNode.leftChild);
			}
			if (curNode.rightChild != null) {
				queue.add(curNode.rightChild);
			}
		}
	}

	/**
	 * another levelOrder: with processing when one level is end
	 * 
	 * @param visitor
	 */
	void levelOrder2(VisitInterface<T> visitor) {
		if (this.root == null) {
			return;
		}
		int levelNum = 0;
		int levelLoc = 0;
		LinkedBlockingQueue<BinaryNode<T>> queue = new LinkedBlockingQueue<BinaryNode<T>>();
		this.root.levelNum = levelNum;
		this.root.levelLoc = 0;
		queue.add(this.root);
		queue.add(new BinaryNode<T>(null));// end label of current level
		levelNum++;
		while (!queue.isEmpty()) {
			BinaryNode<T> curNode = queue.poll();
			BinaryNode<T> following = queue.peek();
			if (visitor != null) {
				visitor.visit(curNode);
			} else {
				visit(curNode);
			}
			if (curNode.leftChild != null) {
				curNode.leftChild.levelNum = levelNum;
				curNode.leftChild.levelLoc = levelLoc++;
				queue.add(curNode.leftChild);
			}
			if (curNode.rightChild != null) {
				curNode.rightChild.levelNum = levelNum;
				curNode.rightChild.levelLoc = levelLoc++;
				queue.add(curNode.rightChild);
			}
			if (following.data == null) {
				queue.poll();// just remove the end label of previous level
				if (queue.peek() == null) {
					// no more data, break
					break;
				}
				queue.add(new BinaryNode<T>(null));// end label of current level
				levelNum++;
				levelLoc = 0;
				if (visitor != null) {
					visitor.visit(null);
				} else {
					visit(null);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void show() {
		System.out.println(toString());
	}

	/**
	 * use levelOrder2(visitor)
	 */
	public String toString() {
		if (this.root == null) {
			return "{}";
		}
		final StringBuffer visitBuffer = new StringBuffer();
		final class _nodePos {
			int value;
		}
		final _nodePos _levelPos = new _nodePos();
		VisitInterface<T> visitor = new VisitInterface<T>() {
			public void visit(BinaryNode<T> node) {
				if (node == null) {
					visitBuffer.append('\n');
					_levelPos.value = 0;
					return;
				}
				if (node.leftChild != null) {
					int llw = node.leftChild.widthLeft() + 1;
					int lw = node.widthLeft() + 1;
					int i = 0;
					for (; i < llw; i++) {
						visitBuffer.append("    ");
						_levelPos.value++;
					}
					visitBuffer.append("┌───");
					_levelPos.value++;
					for (i++; i < lw; i++) {
						visitBuffer.append("────");
						_levelPos.value++;
					}
				}
				if (node.parent != null) {
					BinaryNode<T> p = node.parent;
					int pPos = p.levelPos;
					pPos += (node == p.leftChild) ? (-p.widthLeft() - 1) : (node.widthLeft() + 1);
					while (_levelPos.value < pPos) {
						visitBuffer.append((node.leftChild != null) ? "────" : "    ");
						_levelPos.value++;
					}
				}
				visitBuffer.append("{" + ((String.valueOf(node.data).length() < 2) ? " " : "") + node.data + "}");
				node.levelPos = _levelPos.value;
				_levelPos.value++;
				if (node.rightChild != null) {
					int rlw = node.rightChild.widthLeft() + 1;
					for (int i = 1; i < rlw; i++) {
						visitBuffer.append("────");
						_levelPos.value++;
					}
					visitBuffer.append("───┐");
					_levelPos.value++;
				}
				if (node.parent != null) {
					int pPos = node.parent.levelPos;
					while (_levelPos.value < pPos) {
						visitBuffer.append("    ");
						_levelPos.value++;
					}
				}
			}
		};
		levelOrder2(visitor);
		return visitBuffer.toString();
	}

	/**
	 * 
	 */
	public void show2() {
		System.out.println(toString2());
	}

	/**
	 * use preOrder(visitor)
	 * 
	 * @return
	 */
	public String toString2() {
		if (this.root == null) {
			return "{}";
		}
		// init
		preOrder(new VisitInterface<T>() {
			public void visit(BinaryNode<T> node) {
				if (node != null) {
					node.visited = false;
				}
			}
		});
		final StringBuffer visitBuffer = new StringBuffer();
		final Deque<BinaryNode<T>> pList = new ArrayDeque<BinaryNode<T>>();
		VisitInterface<T> visitor = new VisitInterface<T>() {
			public void visit(BinaryNode<T> node) {
				if (node == null) {
					visitBuffer.append('\n');
					return;
				}
				BinaryNode<T> p = node.parent;
				while (p != null) {
					pList.add(p);
					p = p.parent;
				}
				while (!pList.isEmpty()) {
					p = pList.pollLast();
					if (p == node.parent) {
						if (p.leftChild == node && p.rightChild != null) {
							// left is earlier then right
							visitBuffer.append("├───");
						} else {
							visitBuffer.append("└───");
						}
					} else {
						if ((p.leftChild == null || p.leftChild != null && p.leftChild.visited)
								&& (p.rightChild == null || p.rightChild != null && p.rightChild.visited)) {
							visitBuffer.append("    ");
						} else {
							visitBuffer.append("│   ");
						}
					}
				}
				visitBuffer.append("{" + node.data + "}\n");
				node.visited = true;
			}
		};
		preOrder(visitor);
		return visitBuffer.toString();
	}
}
