package heap;

public class HeapTest {
	public static void main(String[] args) {
		MinHeap<String> strHeap = new MinHeap<String>();
		String[] data = new String[] { "234", "3243", "sdfwe", "fer" };
		strHeap.init(data);
		//String last = strHeap.delete();
		//System.out.println(last);
		Object[] strs = strHeap.toArray();
		for (int i = 0; i < strs.length; i++) {
			System.out.println(strs[i]);
		}

		System.out.println();
		MinHeap<Integer> intHeap = new MinHeap<Integer>();
		Integer[] data2 = new Integer[] { 234, 3243, 1323, 234332 };
		intHeap.init(data2);
		//Integer ilast = intHeap.delete();
		//System.out.println(ilast);
		Integer[] ints = new Integer[intHeap.size()];
		intHeap.toArray(ints);
		for (int i = 0; i < ints.length; i++) {
			System.out.println(ints[i]);
		}
		
		System.out.println();
		RandamDouble[] randDoubles = new RandamDouble[12];
		for(int i=0; i<randDoubles.length; i++) randDoubles[i] = new RandamDouble();
		MaxHeap<RandamDouble> randDoubleHeap = new MaxHeap<RandamDouble>();
		randDoubleHeap.init(randDoubles);
		RandamDouble[] results = new RandamDouble[12];
		randDoubleHeap.toArray(results);
		for (int i = 0; i < results.length; i++) {
			System.out.println(results[i]);
		}
	}
}

class RandamDouble implements Comparable<RandamDouble>{
	double data = Math.random();

	public int compareTo(RandamDouble o) {
		double d1 = this.data, d2 = o.data;
		if (d1 < d2)
            return -1;
        if (d1 == d2)
            return 0;
		return 1;
	}
	
	public String toString(){
		return ""+this.data;
	}
}
