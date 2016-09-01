package basic;

public class WaitTest {
	static final Object lock = new Object();
	public static void main(String[] args) {
		wait_test();
	}

	private static void wait_test() {
		System.out.println("Hello world!");
		
		new Thread(){
			public void run(){
				synchronized (lock) {
					System.out.println("a");
					try {
						lock.wait();
						System.out.println("I am OK!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized(lock){
			System.out.println("Hi");
			int n = (int)(Math.random()*5) + 1;
			while(n > 0) {
				System.out.println(n--);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(n);
			
			lock.notify();
		}
	}
}
