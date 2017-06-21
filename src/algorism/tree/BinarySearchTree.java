package algorism.tree;

import java.util.concurrent.LinkedBlockingQueue;

public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

	/**
	 * 小的元素左走，大的元素右�?
	 */
	public BinaryNode<T> insert(T data) {
		if (this.root == null) {
			this.root = new BinaryNode<T>(data);
			return this.root;
		}

		BinaryNode<T> curNode = this.root;
		while (curNode != null) {
			int comp = data.compareTo(curNode.data);
			if (comp < 0) { // �?
				if (curNode.leftChild == null) {
					curNode.leftChild = new BinaryNode<T>(data, curNode);
					return curNode.leftChild;
				} else {
					curNode = curNode.leftChild;
				}
			} else if (comp == 0) { // 相等
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
			} else { // �?
				if (curNode.rightChild == null) {
					curNode.rightChild = new BinaryNode<T>(data, curNode);
					return curNode.rightChild;
				} else {
					curNode = curNode.rightChild;
				}
			}
		}
		return null;
	}

	public boolean delete(T data) {
		BinaryNode<T> target = search(data, this.root);
		// System.out.println(target);

		if (target == null) {
			return false;
		}
		if (target == this.root) {
			if (this.root.isLeaf()) {
				this.root = null;
				return true;
			} else if (this.root.rightChild == null) {
				this.root.leftChild.parent = null;
				this.root = this.root.leftChild;
				return true;
			} else if (this.root.leftChild == null) {
				this.root.rightChild.parent = null;
				this.root = this.root.rightChild;
				return true;
			}
		}
		// if it has no children or just one child, delete it
		// else two child: fake delete, to get one child data and assign it to target, then delete the child node
		while (target != null) {
			// (1) no children
			if (target.isLeaf()) {
				// delete this node
				BinaryNode<T> parent = target.parent;
				if (parent.leftChild == target) {
					parent.leftChild = null;
				} else {
					parent.rightChild = null;
				}
				break;
			}
			// (2) only one child
			BinaryNode<T> theChild = null;
			if (target.rightChild == null) {
				theChild = target.leftChild;
			} else if (target.leftChild == null) {
				theChild = target.rightChild;
			}
			if (theChild != null) {
				BinaryNode<T> parent = target.parent;
				if (parent.leftChild == target) {
					parent.leftChild = theChild;
				} else {
					parent.rightChild = theChild;
				}
				theChild.parent = parent;
				break;
			}
			// (3) having both leftChild and rightChild
			// 3.1
			T curData = target.data;
			T leftData = target.leftChild.data;
			if (leftData == curData || (curData != null && (curData.compareTo(leftData) == 0))) {
				// transfer target node into its leftChild
				target = target.leftChild;
				continue;
			}
			T rightData = target.rightChild.data;
			if (rightData == curData || (curData != null && (curData.compareTo(rightData) == 0))) {
				// transfer target node into its rightChild
				target = target.rightChild;
				continue;
			}
			// 3.2 to remove target node data, transfer target node deeper
			if (target.leftChild.height() > target.rightChild.height()) {
				// left tree is higher
				// find max-node from left tree
				BinaryNode<T> greaterChild = findMax2(target.leftChild);
				target.data = greaterChild.data;
				target = greaterChild;
			} else {
				// right tree is higher
				// find min-node from right tree
				BinaryNode<T> smallerChild = findMin2(target.rightChild);
				target.data = smallerChild.data;
				target = smallerChild;
			}
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public T findMin() {
		if (this.root == null) {
			return null;
		}
		BinaryNode<T> curNode = this.root;
		while (curNode.leftChild != null) {
			curNode = curNode.leftChild;
		}
		return curNode.data;
	}

	BinaryNode<T> findMin2() {
		return findMin2(this.root);
	}

	protected BinaryNode<T> findMin2(BinaryNode<T> node) {
		if (node != null) {
			while (node.leftChild != null) {
				node = node.leftChild;
			}
		}
		return node;
	}

	/**
	 * 
	 * @return
	 */
	public T findMax() {
		if (this.root == null) {
			return null;
		}
		BinaryNode<T> curNode = this.root;
		while (curNode.rightChild != null) {
			curNode = curNode.rightChild;
		}
		return curNode.data;
	}

	BinaryNode<T> findMax2() {
		return findMax2(this.root);
	}

	protected BinaryNode<T> findMax2(BinaryNode<T> node) {
		if (node == null) {
			return null;
		} else if (node.rightChild == null) {
			return node;
		}
		return findMax2(node.rightChild);
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public BinaryNode<T> search(T target) {
		if (target == null) {
			return null;
		}
		return search(target, this.root);
	}

	/**
	 * 
	 * @param target
	 * @param node
	 * @return
	 */
	protected BinaryNode<T> search(T target, BinaryNode<T> node) {
		if (node != null && node.data != null) {
			// check self
			if (node.data == target || (node.data.compareTo(target) == 0)) {
				return node;
			}
			BinaryNode<T> temp = null;
			int comp = target.compareTo(node.data);
			if (comp < 0) {
				// check left child
				temp = search(target, node.leftChild);
				if (temp != null) {
					return temp;
				}
			} else {
				// check right child
				temp = search(target, node.rightChild);
				if (temp != null) {
					return temp;
				}
			}
		}
		return null;
	}

	/**
	 * construct one sorted array by middle order traverse
	 * 
	 * @return
	 */
	T[] toArray(T[] a) {
		final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();
		middleOrder(new VisitInterface<T>() {
			public void visit(BinaryNode<T> node) {
				queue.add(node.data);
			}
		});
		return queue.toArray(a);
	}
}
