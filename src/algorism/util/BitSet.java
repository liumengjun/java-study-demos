package algorism.util;

public class BitSet {
	
	public BitSet(int N) {
		bits = new char[(N - 1) / 8 + 1];
	}

	public boolean get(int n) {
		return (bits[n >> 3] & (1 << (n & 7))) != 0;
	}

	public void set(int n) {
		bits[n >> 3] |= 1 << (n & 7);
	}

	public void clear(int n) {
		bits[n >> 3] &= ~(1 << (n & 7));
	}

	private char[] bits;
}
