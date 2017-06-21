package algorism.graph;

import java.util.HashMap;

/**
 * Disjoint set class, using union by rank and path compression. Elements in the
 * set are numbered starting at 0. To simulate the disjointed nodes, trees and
 * forests.
 * 
 * @author Scott Lew
 */
public class DisjSets<E extends Comparable<E>> {

	private HashMap<E, E> s;// key(former E) is lower, value(after E) is higher

	/**
	 * Construct the disjoint sets object.
	 * 
	 * @param numElements
	 *            the initial number of disjoint sets.
	 */
	public DisjSets(int numElements) {
		if (numElements < 0) {
			numElements = -numElements;
		}
		if (numElements > (Integer.MAX_VALUE >> 1 - 1)) {
			numElements = Integer.MAX_VALUE >> 1 - 2;
		}
		s = new HashMap<E, E>(numElements * 2 + 1);
	}

	/**
	 * Union two disjoint sets using the height heuristic. For simplicity, we
	 * assume root1 and root2 are distinct and represent set names.
	 * 
	 * @param root1
	 *            the root of set 1.
	 * @param root2
	 *            the root of set 2.
	 */
	public void union(E root1, E root2) {
		E r1 = find(root1);
		E r2 = find(root2);
		if (r2.compareTo(r1) < 0)
			s.put(r1, r2);// Make r2 new root
		else if (r2.compareTo(r1) > 0)
			s.put(r2, r1);// Make r1 new root
		else {
			// they are already in a same forest
		}
	}

	/**
	 * Perform a find with path compression. Error checks omitted again for
	 * simplicity.
	 * 
	 * @param x
	 *            the element being searched for.
	 * @return the set containing x.
	 */
	public E find(E x) {
		if (s.get(x) == null)
			return x;
		else {
			E xr = find(s.get(x));
			s.put(x, xr); // doing this is aimed to speed up executing
			return xr;
		}
	}

	// Test main; all finds on same output line should be identical
	public static void main(String[] args) {
		int NumElements = 128;
		int NumInSameSet = 16;

		DisjSets<Integer> ds = new DisjSets<Integer>(NumElements);
		int set1, set2;

		for (int k = 1; k < NumInSameSet; k *= 2) {
			for (int j = 0; j + k < NumElements; j += 2 * k) {
				set1 = ds.find(j);
				set2 = ds.find(j + k);
				ds.union(set1, set2);
			}
		}

		for (int i = 0; i < NumElements; i++) {
			System.out.print(ds.find(i) + "*");
			if (i % NumInSameSet == NumInSameSet - 1)
				System.out.println();
		}
		System.out.println();
	}
}
