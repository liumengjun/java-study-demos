package _test;

public class ChildOfImplOne extends ImplOne implements InterfaceTwo {
	public void hello() {
		System.out.println("Hei! Hei! Hei!");
	}
	
	public void doSomething() {
		System.out.println("Why let me do that? That's not my dish! I hate it!!!");
	}

	// doNothing() in InterfaceTwo was public, can not assign weaker access protected or default
	public void doNothing(){

	}
}
