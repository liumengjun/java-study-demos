package _test;

public class ImplOne extends OneAbstractClass implements OneInterface {

	public void doSomething() {
		System.out.println("Do Something by No.1!");
	}
	
	public void doOtherThing() {
		System.out.println("I'm NO.1, what else can I do for you?");
	}

	public void end() {
		System.out.println("Well Done!\n");
	}

}
