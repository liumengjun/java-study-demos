package _test;

class TemplateTest<T> {
	public void output(){
		System.out.println();
	}
}

//class TemplateTest2<T extends Comparable<T>> extends TemplateTest<T> {
class TemplateTest2<T extends Comparable<T>> extends TemplateTest<Comparable<T>> {
	public void output(){
		//T obj = new T();
		System.out.println();
	}
	public void compare(T t1, T t2){
		System.out.println(t1.compareTo(t2));
	}
}

public class TemplateTestDemo{
	public static void main(String[] args) {
		TemplateTest2 ttt = new TemplateTest2();
		ttt.output();
	}
}
