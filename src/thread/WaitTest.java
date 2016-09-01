package thread;

public class WaitTest {
	static Object lock = new Object();

	public static void main(String[] args) {
		wait_test();
	}
	
	private static void wait_test() {
		System.out.println("Hi, Hello world!");

		new Thread() {
			public void run() {
				synchronized (lock) {
					// lock.notifyAll();
					try {
						System.out.println("Ha ha ha");
						lock.wait();
						System.out.println("Hey, Hei, I am OK!");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		// next procedure is to wait the above thread to be running.
		// if do not wait, the above thread may be created after main thread is finish, then the {lock.notifyAll();}
		// does not work well.
		// the other way is to un-comment the commented codes in the beginning of {synchronized (lock){}}.
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		synchronized (lock) {
			// try {
			// lock.wait();
			// } catch (InterruptedException e1) {
			// e1.printStackTrace();
			// }
			System.out.println("a a...");
			int n = (int) (Math.random() * 5) + 1;
			while (n > 0) {
				System.out.println(n--);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(n);
			lock.notifyAll();
		}
	}
	
}
