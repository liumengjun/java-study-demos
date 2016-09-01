

public class E {
	
	int a;
	double b;
	String c;
	
	void foo1() throws IllegalArgumentException{}
	void foo2() throws IllegalArgumentException{}
	void foo3(){
		double b1;
		//double d = b+b1;//b1未初始化
		double d = b+b;
		b = d;
		System.out.println(b);
	}
	
	public static void main(String[] args) {
		try{
			E e = new E();
			System.out.println(e.a);
			System.out.println(e.b);
			System.out.println(e.c);
			e.foo3();
			
			System.out.println();
			System.out.println(0);
			//System.exit(0);
			if(1==(int)Math.rint(1.001)){
				return;
			}
			System.out.println(1);
			return;
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			System.out.println(2);
			new Ee().foo5();
			//return;//No need
		}
	}
}
class Ee extends E {
	private int hi = 2333;
	void foo1(){}
	void foo2() throws IllegalStateException{
		super.foo2();
	}
	void foo3() throws IllegalStateException{}
	void foo5(){
		System.out.println(new EeInner().hey);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(this+" :"+this.getClass());
				System.out.println(Ee.this+" :"+Ee.this.getClass());
			}
		}).start();
	}
	
	private class EeInner{
		private String hey = "hey";
		void foo4(){
			System.out.println(hi);
			//System.out.println(this.hi);
			System.out.println(Ee.this.hi);
		}
	}
}
