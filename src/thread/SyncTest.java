package thread;

import java.util.Vector;

public class SyncTest {
	static Vector<Integer> viList = new Vector<Integer>();
	static int i = 0;
	public static void main(String[] args) throws Exception {
		new Thread() {
			public void run() {
				synchronized (viList) {
					int n=10;
					while(n>1){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (n==5) {
							try {
								viList.wait();// yield
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						System.out.println(n);
						viList.add(n--);
					}
					viList.notify();
					System.out.println(viList);
				}
			}
		}.start();
		Thread.sleep(100);
		
		new Thread() {
			public void run() {
				synchronized (viList) {
					int n=10;
					while(n<20){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(n);
						viList.add(n++);
					}
					viList.notify();
					System.out.println(viList);
				}
			}
		}.start();
		Thread.sleep(100);
		
		Thread.sleep(1000);
		Thread.yield();
		System.out.println(viList);
	}
}
