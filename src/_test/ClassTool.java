package _test;

interface A {

}

interface B extends A{
	
}

class A1 implements A{
	protected void addChild(A11 a11){
		System.out.println(a11);
	}
}

class A11 extends A1{
//	String toString(){
//		return "";
//	}
}

class B1 implements B{
	void testA1(A1 a1){
//		a1.addChild(new A1(){});
		a1.addChild(new A11(){
			public void print(){}// The method print() from the type new A11(){} is never used locally
		});
	}
}

class B11 extends B1{
	
}


class AB1 extends B1 implements A{
	
}

class BA1 extends A1 implements B{
	
}

public class ClassTool{
	static boolean checkAIsClassOrChildClassOfB(Class a, Class b) {
		if (a.equals(b)) {
			return true;
		}
		if (a.isInterface()) {
			Class aI[] = a.getInterfaces();
			for (int i=0; i<aI.length; i++) {
				Class curI = aI[i];
				if (checkAIsClassOrChildClassOfB(curI, b)) {
					return true;
				}
			}
		} else {
			if (a.getSuperclass() != null) {
				return checkAIsClassOrChildClassOfB(a.getSuperclass(), b);
			}
		}
		return false;
	}
	
	static boolean checkAImplementsOfB(Class a, Class b) {
		if (!b.isInterface()) {
			return false;
		}
		Class aI[] = a.getInterfaces();
		for (int i=0; i<aI.length; i++) {
			Class curI = aI[i];
			if (checkAIsClassOrChildClassOfB(curI, b)) {
				return true;
			}
		}
		if (a.getSuperclass() != null) {
			return checkAImplementsOfB(a.getSuperclass(), b);
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println((new B11() instanceof A));
		System.out.println((new BA1() instanceof A));
		System.out.println((new AB1() instanceof B));
		System.out.println(checkAImplementsOfB(B11.class, A.class));
		System.out.println(checkAImplementsOfB(BA1.class, A.class));
		System.out.println(checkAImplementsOfB(AB1.class, B.class));
	}
}