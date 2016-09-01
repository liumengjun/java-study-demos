package heap;

import java.util.Arrays;

public class MinHeap<T extends Comparable<T>> extends AbstractHeap<T> {

	@SuppressWarnings("unchecked")
	public void insert(T data) {
		if (this.elements == null) {
			this.elements = new Object[8];
		}
		if (size == this.elements.length) {
			this.elements = Arrays.copyOf(this.elements, size + 16);
		}
		this.elements[size] = data;
		size++;
		{// 若子节点元素小于父节点，则向上移动
			int j = size - 1, i = (j - 1) / 2; // i指向j的父节点
			Object temp = elements[j];
			while (j > 0) {
				if (((T) elements[i]).compareTo((T) temp) <= 0) {
					break;
				} else {
					elements[j] = elements[i];
					j = i;
					i = (i - 1) / 2;
				}
			}
			elements[j] = temp;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public T delete() {
		if (this.size == 0) {
			return null;
		}
		T ele = (T) this.elements[0];
		this.elements[0] = this.elements[size - 1];
		size--;
		this.elements[size] = null;
		int start = 0, end = size - 1;
		{// 若父节点元素大于子父节点，则向下移动
			int i = start, j = 2 * i + 1;// i指向j的父节点
			Object temp = elements[i];
			while (j <= end) {
				if ((j < end) && (((T) elements[j]).compareTo((T) elements[j + 1]) > 0)) {
					j++;
				}
				if (((T) temp).compareTo((T) elements[j]) <= 0) {
					break;
				} else {
					elements[i] = elements[j];
					i = j;
					j = 2 * j + 1;
				}
			}
			elements[i] = temp;
		}
		return ele;
	}

	@Override
	public Heap<T> heapify() {
		// TODO Auto-generated method stub
		return null;
	}

}
