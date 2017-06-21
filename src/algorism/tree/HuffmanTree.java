package algorism.tree;

import algorism.heap.MinHeap;

public class HuffmanTree extends BinaryTree<Double> {

	public HuffmanTree() {
	}

	public HuffmanTree(double[] probability) {
		this.init(probability);
	}

	public void init(double[] probability) {
		if (probability == null) {
			return;
		}
		int N = probability.length;
		HuffmanNode[] huffmanNodes = new HuffmanNode[N];
		for (int i = 0; i < N; i++) {
			huffmanNodes[i] = new HuffmanNode(probability[i]);
		}

		// init min heap
		MinHeap<HuffmanNode> minHeap = new MinHeap<HuffmanNode>();
		minHeap.init(huffmanNodes);

		// construct huffman tree
		while (minHeap.size() > 0) {
			HuffmanNode a = minHeap.delete();
			HuffmanNode b = minHeap.delete();
			if (b == null) {
				this.root = a;
				break;
			}
			HuffmanNode c = new HuffmanNode(a.data + b.data, a, b);// a<b, 小的在左，大的在�?
			minHeap.insert(c);
		}
	}

	public String getCode(double probability) {
		StringBuffer codeBuf = new StringBuffer();
		codeBuf.setLength(this.height());
		// single thread
		search(probability, (HuffmanNode) this.root, 0, codeBuf);
		return codeBuf.toString();
	}

	private HuffmanNode search(Double target, HuffmanNode node, int depth, StringBuffer codeBuf) {
		if (node != null) {
			// check self
			if (node.isLeaf() && (node.data == target || node.data.compareTo(target) == 0)) {
				codeBuf.setLength(depth);
				return node;
			}
			HuffmanNode temp = null;
			// check left child, set code '0'
			codeBuf.setCharAt(depth, '0');
			temp = search(target, (HuffmanNode) node.leftChild, depth + 1, codeBuf);
			if (temp != null) {
				return temp;
			}
			// check right child, set code '1'
			codeBuf.setCharAt(depth, '1');
			temp = search(target, (HuffmanNode) node.rightChild, depth + 1, codeBuf);
			if (temp != null) {
				return temp;
			}
		}
		return null;
	}

	public HuffmanNode search(Double target) {
		return search(target, (HuffmanNode) this.root);
	}

	private HuffmanNode search(Double target, HuffmanNode node) {
		if (node != null) {
			// check self
			if (node.isLeaf() && (node.data == target || node.data.compareTo(target) == 0)) {
				return node;
			}
			HuffmanNode temp = null;
			// check left child
			temp = search(target, (HuffmanNode) node.leftChild);
			if (temp != null) {
				return temp;
			}
			// check right child
			temp = search(target, (HuffmanNode) node.rightChild);
			if (temp != null) {
				return temp;
			}
		}
		return null;
	}

	private class HuffmanNode extends BinaryNode<Double> implements Comparable<HuffmanNode> {
		Object key;

		public HuffmanNode(Double probability) {
			super(probability);
		}

		public HuffmanNode(Double probability, HuffmanNode left, HuffmanNode right) {
			super(probability, null, left, right);
			left.parent = this;
			right.parent = this;
		}

		public int compareTo(HuffmanNode o) {
			double d1 = this.data, d2 = o.data;
			if (d1 < d2)
				return -1;
			if (d1 == d2)
				return 0;
			return 1;
		}
	}
}
