package tree;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {

	public BinaryNode<T> insert(T data) {
		if (this.root == null) {
			this.root = new BinaryNode<T>(data);
			this.root.updateHeight();
			return this.root;
		}

		BinaryNode<T> curNode = this.root;
		while (curNode != null) {
			int comp = data.compareTo(curNode.data);
			if (comp <= 0) { // 小 或 相等
				if (curNode.leftChild == null) {
					BinaryNode<T> newNode = new BinaryNode<T>(data, curNode);
					curNode.leftChild = newNode;
					balance(curNode);
					return newNode;
				} else {
					curNode = curNode.leftChild;
				}
			} else { // 大
				if (curNode.rightChild == null) {
					BinaryNode<T> newNode = new BinaryNode<T>(data, curNode);
					curNode.rightChild = newNode;
					balance(curNode);
					return newNode;
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
					balance(parent);
				} else {
					parent.rightChild = null;
					balance(parent);
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
					theChild.parent = parent;
					balance(parent);
				} else {
					parent.rightChild = theChild;
					theChild.parent = parent;
					balance(parent);
				}
				break;
			}
			// (3) having both leftChild and rightChild
			// 3.1
			T curData = target.data;
			T rightData = target.rightChild.data;
			if (rightData == curData || (curData != null && (curData.compareTo(rightData) == 0))) {
				// transfer target node into its rightChild
				target = target.rightChild;
				continue;
			}
			// 3.2 to remove target node data, transfer target node deeper
			if (target.leftChild.height > target.rightChild.height) {
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
	 */
	public int height() {
		return BinaryNode.getHeight(this.root);
	}

	private BinaryNode<T> balance(BinaryNode<T> node) {
		if (node == null) {
			return null;
		}
		int oldHeight = node.height;
		node.updateHeight();
		int newHeight = node.height;
		if (oldHeight == newHeight) {
			return node; // no change, just level
		}
		BinaryNode<T> pNode = node.parent;
		if (BinaryNode.getHeight(node.leftChild) - BinaryNode.getHeight(node.rightChild) >= 2) {
			BinaryNode<T> kNode;
			if (BinaryNode.getHeight(node.leftChild.leftChild) >= BinaryNode.getHeight(node.leftChild.rightChild)) {
				kNode = rotateWithLeftChild(node);
			} else {
				kNode = doubleWithLeftChild(node);
			}
			if (pNode == null) {
				this.root = kNode;
			} else {
				if (pNode.leftChild == node) {
					pNode.leftChild = kNode;
				} else {
					pNode.rightChild = kNode;
				}
			}
			balance(kNode);
		}
		if (BinaryNode.getHeight(node.rightChild) - BinaryNode.getHeight(node.leftChild) >= 2) {
			BinaryNode<T> kNode;
			if (BinaryNode.getHeight(node.rightChild.rightChild) >= BinaryNode.getHeight(node.rightChild.leftChild)) {
				kNode = rotateWithRightChild(node);
			} else {
				kNode = doubleWithRightChild(node);
			}
			if (pNode == null) {
				this.root = kNode;
			} else {
				if (pNode.leftChild == node) {
					pNode.leftChild = kNode;
				} else {
					pNode.rightChild = kNode;
				}
			}
			balance(kNode);
		}
		// go to upper
		if (pNode != null) {
			balance(pNode);
		}
		return node;
	}

	/**
	 * Rotate binary tree node with left child. For AVL trees, this is a single rotation for case 1. Update heights,
	 * then return new root.
	 */
	private BinaryNode<T> rotateWithLeftChild(BinaryNode<T> k2) {
		BinaryNode<T> k2p = k2.parent;
		BinaryNode<T> k1 = k2.leftChild;
		BinaryNode<T> Y = k1.rightChild;
		k2.leftChild = Y;
		if (Y != null) {
			Y.parent = k2;
		}
		k1.rightChild = k2;
		k2.parent = k1;
		k1.parent = k2p;
		k2.updateHeight();
		// k1.updateHeight();
		return k1;
	}

	/**
	 * Rotate binary tree node with right child. For AVL trees, this is a single rotation for case 4. Update heights,
	 * then return new root.
	 */
	private BinaryNode<T> rotateWithRightChild(BinaryNode<T> k1) {
		BinaryNode<T> k1p = k1.parent;
		BinaryNode<T> k2 = k1.rightChild;
		BinaryNode<T> Y = k2.leftChild;
		k1.rightChild = Y;
		if (Y != null) {
			Y.parent = k1;
		}
		k2.leftChild = k1;
		k1.parent = k2;
		k2.parent = k1p;
		k1.updateHeight();
		// k2.updateHeight();
		return k2;
	}

	/**
	 * Double rotate binary tree node: first left child with its right child; then node k3 with new left child. For AVL
	 * trees, this is a double rotation for case 2. Update heights, then return new root.
	 */
	private BinaryNode<T> doubleWithLeftChild(BinaryNode<T> k3) {
		k3.leftChild = rotateWithRightChild(k3.leftChild);
		k3.leftChild.updateHeight();
		BinaryNode<T> k2 = rotateWithLeftChild(k3);
		// k2.updateHeight();
		return k2;
	}

	/**
	 * Double rotate binary tree node: first right child with its left child; then node k1 with new right child. For AVL
	 * trees, this is a double rotation for case 3. Update heights, then return new root.
	 */
	private BinaryNode<T> doubleWithRightChild(BinaryNode<T> k1) {
		k1.rightChild = rotateWithLeftChild(k1.rightChild);
		k1.rightChild.updateHeight();
		BinaryNode<T> k2 = rotateWithRightChild(k1);
		// k2.updateHeight();
		return k2;
	}
}
