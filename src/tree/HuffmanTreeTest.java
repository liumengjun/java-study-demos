package tree;

import java.util.Arrays;
import java.util.Random;

public class HuffmanTreeTest {
	public static void main(String[] args) {
		HuffmanTree tree = new HuffmanTree();

		int N = 128;
		double[] probabilities = new double[N];
		double total = 0;
		for (int i = 0; i < N; i++) {
			probabilities[i] = new Random().nextInt(2 * N) + 1;
			total += probabilities[i];
		}
		for (int i = 0; i < N; i++) {
			probabilities[i] = probabilities[i] / total;
		}

		tree.init(probabilities);

		System.out.println("height: " + tree.height());
		System.out.println("weight: " + tree.weight());
		System.out.println();

		tree.preOrder();
		System.out.println();
		tree.middleOrder();
		System.out.println();
		tree.postOrder();
		System.out.println();
		tree.levelOrder();
		System.out.println();
		tree.show();

		Arrays.sort(probabilities);
		for (int i = 0; i < N; i++) {
			System.out.printf("%4f code: " + tree.getCode(probabilities[i]) + "\n", probabilities[i]);
		}
	}
}
