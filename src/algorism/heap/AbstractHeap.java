package algorism.heap;

import java.util.Arrays;

public abstract class AbstractHeap<T> implements Heap<T>, Cloneable {

	protected Object[] elements;
	protected int size;

	public void build(int n) {
		int c = geAndMin2ExpNum(n);
		elements = new Object[c];
		size = 0;
	}

	public void init(T[] data) {
		if (data == null) {
			return;
		}
		int c = geAndMin2ExpNum(data.length);
		elements = new Object[c];
		for (int i = 0; i < data.length; i++) {
			insert(data[i]);
		}
		size = data.length;
	}

	public abstract void insert(T data);

	public abstract void update();

	@SuppressWarnings("unchecked")
	public T get() {
		if (elements == null) {
			return null;
		}
		return (T) elements[0];
	}

	public abstract T delete();

	public abstract Heap<T> heapify();

	public Object[] toArray() {
		AbstractHeap<T> tempHeap = this.clone();
		if (tempHeap == null) {
			return null;
		}
		int num = this.size;
		Object[] result = new Object[num];
		for (int i = 0; i < num; i++) {
			result[i] = tempHeap.delete();
		}
		tempHeap = null;
		return result;
	}

	public T[] toArray(T[] a) {
		if (a == null) {
			return null;
		}
		AbstractHeap<T> tempHeap = this.clone();
		if (tempHeap == null) {
			return null;
		}
		int num = Math.min(a.length, this.size);
		for (int i = 0; i < num; i++) {
			a[i] = tempHeap.delete();
		}
		tempHeap = null;
		return a;
	}

	@SuppressWarnings("unchecked")
	public AbstractHeap<T> clone() {
		AbstractHeap<T> clone = null;
		try {
			clone = (AbstractHeap<T>) super.clone();
			clone.elements = Arrays.copyOf(this.elements, this.size);
		} catch (CloneNotSupportedException ex) {
			System.out.println("catch Exception : AbstractHeap<T> clone");
			throw new InternalError(ex.toString());
		}
		return clone;
	}

	public int size() {
		return size;
	}

	public int capacity() {
		if (elements == null) {
			return 0;
		}
		return elements.length;
	}

	public boolean contains(T data) {
		if (elements == null) {
			return false;
		}
		for (int i = 0; i < elements.length; i++) {
			if (data == elements[i] || data.equals(elements[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 求大于等于n的最小的2的指�?<br/>
	 * Find the number that is greater than or equal to n, and it is two's
	 * exponent
	 * 
	 * @param n
	 * @return min{ 2^k>=n,k∈N+ }
	 */
	protected static int geAndMin2ExpNum(int n) {
		// n=1，返�?2，�?�不�?1；否则，后面算法求的�?1
		if (n <= 2) {
			return 2;
		}
		// 求二进制下最高位的位�?
		int temp = n >> 1;
		int count = 0;
		while (temp > 0) {
			count++;
			temp >>= 1;
		}
		// 构�?�只有最高位�?1，后面都�?0的二进制�?
		int expect = 1;
		while (count > 0) {
			expect <<= 1;
			count--;
		}
		// 若n�?高位后面还有1，把期望结果扩大�?�?
		if ((expect ^ n) > 0) {
			expect <<= 1;
		}
		return expect;
	}
}
