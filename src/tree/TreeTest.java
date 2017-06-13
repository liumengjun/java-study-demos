package tree;

import java.util.Random;

public class TreeTest {

	public static void main(String[] args) {
		testTree(args);
		// testBST(args);
		// testAVL(args);
		// testAVL2();
	}

	public static void testAVL2() {
		// AVLTree<Integer> tree = new AVLTree<Integer>();
		AVLTree<Character> tree = new AVLTree<Character>();
		// do test
		for (int key = 1, value = 'a'; value <= 'z'; value++, key++) {
			// tree.insert(key);
			tree.insert((char) value);
		}

		System.out.println("size: " + tree.size());
		System.out.println("show:");
		tree.show();
		System.out.println("show2:");
		tree.show2();
	}

	public static void testAVL(String[] args) {
		AVLTree<Integer> tree = new AVLTree<Integer>();

		int N = 5 + new Random().nextInt(100);
		// int[] num = new int[] { 3, 2, 1, 4, 5, 6, 7, 16, 15, 14, 13, 12, 11, 10, 8, 9 };
		int[] num = new int[N];
		for (int i = 0; i < N; i++) {
			int data = new Random().nextInt(2 * N) + 1;
			num[i] = data;
		}
		for (int i = 0; i < num.length; i++) {
			// System.out.println(num[i]+",");
			tree.insert(num[i]);
		}
		System.out.println();
		System.out.println("height: " + tree.height());
		System.out.println("weight: " + tree.weight());
		System.out.println("width: " + tree.width());
		System.out.println("width left: " + tree.widthLeft());
		System.out.println("width right: " + tree.widthRight());
		System.out.println("minimum: " + tree.findMin() + ",  maximum: " + tree.findMax());
		System.out.println();

		System.out.print("   preOrder:");
		tree.preOrder();
		System.out.println();
		System.out.print("middleOrder:");
		tree.middleOrder();
		System.out.println();
		System.out.print("  postOrder:");
		tree.postOrder();
		System.out.println();
		System.out.print(" levelOrder:");
		tree.levelOrder();
		System.out.println();

		System.out.println("show:");
		tree.show();
		System.out.println("show2:");
		tree.show2();

		// for (int i = 0; i < 2 * N + 2; i++) {
		// System.out.println(tree.contains(i));
		// }

		Integer[] a = new Integer[tree.size()];
		a = tree.toArray(a);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + ",");
		}
		System.out.println();

		System.out.println();
		for (int i = 1; i <= num.length / 2; i++) {
			int target = num[new Random().nextInt(num.length)];
			boolean flag = tree.delete(target);
			// System.out.println("to delete " + target + ", result = " + flag);
		}

		System.out.println();
		System.out.println("height: " + tree.height());
		System.out.println("weight: " + tree.weight());
		System.out.println("width: " + tree.width());
		System.out.println("width left: " + tree.widthLeft());
		System.out.println("width right: " + tree.widthRight());
		System.out.println("minimum: " + tree.findMin() + ",  maximum: " + tree.findMax());
		System.out.println();

		System.out.print("   preOrder:");
		tree.preOrder();
		System.out.println();
		System.out.print("middleOrder:");
		tree.middleOrder();
		System.out.println();
		System.out.print("  postOrder:");
		tree.postOrder();
		System.out.println();
		System.out.print(" levelOrder:");
		tree.levelOrder();
		System.out.println();

		System.out.println("show:");
		tree.show();

		// for (int i = 0; i < 2 * N + 2; i++) {
		// System.out.println(tree.contains(i));
		// }

		Integer[] a1 = new Integer[tree.size()];
		a1 = tree.toArray(a1);
		for (int i = 0; i < a1.length; i++) {
			System.out.print(a1[i] + ",");
		}
		System.out.println();
	}

	public static void testBST(String[] args) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();

		int N = 5 + new Random().nextInt(20);
		for (int i = 1; i <= N; i++) {
			int data = new Random().nextInt(2 * N) + 1;
			tree.insert(data);
		}
		tree.postOrder(new BinaryTree.VisitInterface<Integer>() {
			public void visit(BinaryNode<Integer> node) {
				node.updateHeight();
			}
		});
		System.out.println("height: " + tree.height());
		System.out.println("height2: " + tree.root.height);
		System.out.println("weight: " + tree.weight());
		System.out.println("width: " + tree.width());
		System.out.println("width left: " + tree.widthLeft());
		System.out.println("width right: " + tree.widthRight());
		System.out.println("minimum: " + tree.findMin() + ",  maximum: " + tree.findMax());
		System.out.println();

		System.out.print("   preOrder:");
		tree.preOrder();
		System.out.println();
		System.out.print("middleOrder:");
		tree.middleOrder();
		System.out.println();
		System.out.print("  postOrder:");
		tree.postOrder();
		System.out.println();
		System.out.print(" levelOrder:");
		tree.levelOrder();
		System.out.println();
		System.out.println("show:");
		tree.show();

		// for (int i = 0; i < 2 * N + 2; i++) {
		// System.out.println(tree.contains(i));
		// }

		Integer[] a = new Integer[tree.size()];
		a = tree.toArray(a);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + ",");
		}
		System.out.println();

		System.out.println();
		for (int i = 1; i <= N / 2; i++) {
			int target = new Random().nextInt(2 * N) + 1;
			System.out.println("to delete " + target + ", result = " + tree.delete(target));
		}

		tree.postOrder(new BinaryTree.VisitInterface<Integer>() {
			public void visit(BinaryNode<Integer> node) {
				node.updateHeight();
			}
		});
		System.out.println();
		System.out.println("height: " + tree.height());
		System.out.println("height2: " + tree.root.height);
		System.out.println("weight: " + tree.weight());
		System.out.println("width: " + tree.width());
		System.out.println("width left: " + tree.widthLeft());
		System.out.println("width right: " + tree.widthRight());
		System.out.println("minimum: " + tree.findMin() + ",  maximum: " + tree.findMax());
		System.out.println();

		System.out.print("   preOrder:");
		tree.preOrder();
		System.out.println();
		System.out.print("middleOrder:");
		tree.middleOrder();
		System.out.println();
		System.out.print("  postOrder:");
		tree.postOrder();
		System.out.println();
		System.out.print(" levelOrder:");
		tree.levelOrder();
		System.out.println();
		System.out.println("show:");
		tree.show();

		// for (int i = 0; i < 2 * N + 2; i++) {
		// System.out.println(tree.contains(i));
		// }

		Integer[] a1 = new Integer[tree.size()];
		a1 = tree.toArray(a1);
		for (int i = 0; i < a1.length; i++) {
			System.out.print(a1[i] + ",");
		}
		System.out.println();
	}

	public static void testTree(String[] args) {
		BinaryTree<Integer> tree = new BinaryTree<Integer>();

		int N = 5 + new Random().nextInt(20);
		for (int i = 1; i <= N; i++) {
			tree.insert(i);
		}
		System.out.println("height: " + tree.height());
		System.out.println("weight: " + tree.weight());
		System.out.println("width: " + tree.width());
		System.out.println("width left: " + tree.widthLeft());
		System.out.println("width right: " + tree.widthRight());
		System.out.println();

		System.out.print("   preOrder:");
		tree.preOrder();
		System.out.println();
		System.out.print("middleOrder:");
		tree.middleOrder();
		System.out.println();
		System.out.print("  postOrder:");
		tree.postOrder();
		System.out.println();
		System.out.print(" levelOrder:");
		tree.levelOrder();
		System.out.println();
		System.out.println("show:");
		tree.show();
		System.out.println("show2:");
		tree.show2();
		System.out.println(" levelOrder in Zigzag:");
		tree.levelOrderInZigzag();

		for (int i = 0; i < N + 2; i++) {
			System.out.println("contains(" + i + "): " + tree.contains(i));
		}

		System.out.println();
		for (int i = 1; i <= N / 2; i++) {
			int target = new Random().nextInt(2 * N) + 1;
			System.out.println("to delete " + target + ", result = " + tree.delete(target));
		}

		System.out.println();
		System.out.println("height: " + tree.height());
		System.out.println("weight: " + tree.weight());
		System.out.println("width: " + tree.width());
		System.out.println("width left: " + tree.widthLeft());
		System.out.println("width right: " + tree.widthRight());
		System.out.println();

		System.out.print("   preOrder:");
		tree.preOrder();
		System.out.println();
		System.out.print("middleOrder:");
		tree.middleOrder();
		System.out.println();
		System.out.print("  postOrder:");
		tree.postOrder();
		System.out.println();
		System.out.print(" levelOrder:");
		tree.levelOrder();
		System.out.println();
		System.out.println("show:");
		tree.show();
		System.out.println("show2:");
		tree.show2();
		System.out.println(" levelOrder in Zigzag:");
		tree.levelOrderInZigzag();

		for (int i = 0; i < N + 2; i++) {
			System.out.println("contains(" + i + "): " + tree.contains(i));
		}
	}
}
