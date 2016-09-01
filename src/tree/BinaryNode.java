package tree;

public class BinaryNode<T> {
	T data;
	BinaryNode<T> leftChild;
	BinaryNode<T> rightChild;
	BinaryNode<T> parent;
	int height;

	boolean visited; // only for traversing tree;
	int levelNum;// the level NO. in the tree, start from 0, only for traversing tree in level-order;
	int levelLoc;// the level NO. in the tree, start from 0, only for traversing tree in level-order;
	int levelPos;// the pos on the level, only for traversing tree in level-order;

	public BinaryNode(T data) {
		this(data, null);
	}

	public BinaryNode(T data, BinaryNode<T> parent) {
		this(data, parent, null, null);
	}

	public BinaryNode(T data, BinaryNode<T> parent, BinaryNode<T> left, BinaryNode<T> right) {
		this.data = data;
		this.parent = parent;
		this.leftChild = left;
		this.rightChild = right;
		if (left != null) {
			left.parent = this;
		}
		if (right != null) {
			right.parent = this;
		}
		updateHeight();
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	static <T> int height(BinaryNode<T> node) {
		if (node == null) {
			return 0;
		}
		int hL = height(node.leftChild);
		int hR = height(node.rightChild);
		int h = ((hL > hR) ? hL : hR) + 1;
		return h;
	}

	/**
	 * 
	 * @return
	 */
	int height() {
		return height(this);
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	static <T> int getHeight(BinaryNode<T> node) {
		return node == null ? 0 : node.height;
	}

	void updateHeight() {
		this.height = this.height2();
	}

	private int height2() {
		int hL = getHeight(this.leftChild);
		int hR = getHeight(this.rightChild);
		int h = ((hL > hR) ? hL : hR) + 1;
		return h;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	static <T> int weight(BinaryNode<T> node) {
		if (node == null) {
			return 0;
		}
		int wL = weight(node.leftChild);
		int wR = weight(node.rightChild);
		return wL + wR + 1;
	}

	/**
	 * 
	 * @return
	 */
	int weight() {
		return weight(this);
	}

	static <T> int width(BinaryNode<T> node) {
		if (node == null) {
			return 0;
		}
		int wL = widthLeft(node);
		int wR = widthRight(node);
		return wL + wR + 1;
	}

	static <T> int widthLeft(BinaryNode<T> node) {
		// the width of node's left side, this node do not count
		if (node == null || node.leftChild == null) {
			return 0;
		}
		int wL = widthLeft(node.leftChild) + 1;
		if (node.leftChild.rightChild != null) {
			int wLR = widthLeft(node.leftChild.rightChild);
			wL = wL > wLR ? wL : wLR;
			wL += widthRight(node.leftChild.rightChild) + 1;
		}
		return wL;
	}

	static <T> int widthRight(BinaryNode<T> node) {
		// the width of right's left side, this node do not count
		if (node == null || node.rightChild == null) {
			return 0;
		}
		int wR = widthRight(node.rightChild) + 1;
		if (node.rightChild.leftChild != null) {
			int wRL = widthRight(node.rightChild.leftChild);
			wR = wR > wRL ? wR : wRL;
			wR += widthLeft(node.rightChild.leftChild);
		}
		return wR;
	}

	int width() {
		return width(this);
	}

	int widthLeft() {
		return widthLeft(this);
	}

	int widthRight() {
		return widthRight(this);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return (this.leftChild == null && this.rightChild == null);
	}

	/**
	 * 
	 */
	public String toString() {
		String leftString = null;
		if (this.leftChild == null) {
			leftString = "null";
		} else {
			leftString = String.valueOf(this.leftChild.data);
		}

		String rightString = null;
		if (this.rightChild == null) {
			rightString = "null";
		} else {
			rightString = String.valueOf(this.rightChild.data);
		}

		return "{BinaryNode:" + String.valueOf(this.data) + "[Left:" + leftString + "," + "Right:" + rightString + "]}";
	}
}
