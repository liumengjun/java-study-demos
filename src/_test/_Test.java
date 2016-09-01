package _test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

public class _Test {
	protected static int a = 1;
	static int b = 2;
	public static void main(String[] args) throws IOException {
		classRelation();
	}

	public static void classRelation() {
		OneAbstractClass a1 = new ImplOne();
		OneAbstractClass a2 = new ImplTwo();
		OneAbstractClass ca1 = new ChildOfImplOne();

		// a1.doSomething();
		// a1.doOtherThing();
		// a2.doSomething();
		// a2.doOtherThing();
		//
		// System.out.println();

		OneInterface i1 = new ImplOne();
		OneInterface i2 = new ImplTwo();

		// i1.doSomething();
		// i1.doOtherThing();
		// i2.doSomething();
		// i2.doOtherThing();
		//
		// System.out.println();

		try {
			// OneInterface oi1 = (OneInterface) ca1;
			InterfaceTwo oi1 = (InterfaceTwo) ca1;
			oi1.doSomething();
			// oi1.doOtherThing();
			// oi1.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// OneAbstractClass oa2 = (OneAbstractClass) i2;
			InterfaceTwo oa2 = (InterfaceTwo) i2;
			oa2.doSomething();
			// oa2.doOtherThing();
			// oa2.allRight();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
