package _test.sub2.inner;

import _test.sub2.*;

public class C extends B{
	public int B1 = 9;
	public C() {
		super();
		System.out.println(A1);
		System.out.println(A2);
		//System.out.println(A3);
		System.out.println(B1);
	}
	public static void main(String[] args) {
		new C();
	}
}
