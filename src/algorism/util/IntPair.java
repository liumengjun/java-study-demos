package algorism.util;

/**
 * integer pair(整数对)
 * @author zhonglijunyi
 *
 */
public class IntPair {
	private int data1;
	private int data2;
	
	public IntPair() {
		super();
		data1 = 0;
		data2 = 0;
	}
	public IntPair(int data1, int data2) {
		super();
		this.data1 = data1;
		this.data2 = data2;
	}
	public int getData1() {
		return data1;
	}
	public void setData1(int data1) {
		this.data1 = data1;
	}
	public int getData2() {
		return data2;
	}
	public void setData2(int data2) {
		this.data2 = data2;
	}
	
}
