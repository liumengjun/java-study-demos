package algorism.heap;

/**
 * 堆（英语：heap)是计算机科学中一类特殊的数据结构的统称�?�堆通常是一个可以被看做�?棵树的数组对象�?? 堆�?�是满足下列性质�?<br/>
 * <br/>
 * 堆中某个节点的�?��?�是大于或小于其父节点的值；<br/>
 * 堆�?�是�?颗完全树�?<br/>
 * <br/>
 * 将根节点�?大的堆叫做最大堆或大根堆，根节点�?小的堆叫做最小堆或小根堆。常见的堆有二叉堆�?�斐波那契堆等�??<br/>
 * 
 * @author 21714900R2960
 * 
 * @param <T>
 */
public interface Heap<T> {

	/**
	 * 建立�?个空�?
	 */
	public void build(int n);

	/**
	 * 根据指定的数组，构�?�一个堆
	 * 
	 * @param data
	 */
	public void init(T[] data);

	/**
	 * 向堆中插入一个新元素
	 * 
	 * @param data
	 */
	public void insert(T data);

	/**
	 * 将新元素提升使其符合堆的性质
	 */
	public void update();

	/**
	 * 获取当前堆顶元素的�??
	 * 
	 * @return
	 */
	public T get();

	/**
	 * 删除堆顶元素
	 * 
	 * @return
	 */
	public T delete();

	/**
	 * 使删除堆顶元素的堆再次成为堆
	 * 
	 * @return
	 */
	public Heap<T> heapify();

	/**
	 * 返回排序完好的T对象数组
	 * 
	 * @return
	 */
	public Object[] toArray();

	/**
	 * 返回排序完好的T对象数组
	 * 
	 * @param a
	 *            特定大小的空数组
	 * @return
	 */
	public T[] toArray(T[] a);

	public int size();

	public int capacity();

	public boolean contains(T data);
}
