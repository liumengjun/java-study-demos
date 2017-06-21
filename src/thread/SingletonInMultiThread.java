package thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SingletonInMultiThread {
	/* maybe volatile is not in need. */
	// private static volatile AtomicInteger instance = null;
	private static AtomicInteger instance1st = null;
	private static AtomicInteger instance2nd = null;
	private static final Object lock = new Object();
	private static final ReentrantLock sync = new ReentrantLock();

	/**
	 * 获取单例方法 - synchronized整个方法
	 */
	static synchronized AtomicInteger getInstance1st() {
		if (instance1st == null) {
			System.out.println("new 1st");
			instance1st = new AtomicInteger();
		}
		return instance1st;
	}

	/**
	 * 获取单例方法 - synchronized创建对象段
	 */
	static AtomicInteger getInstance2nd() {
		if (instance2nd == null) {
			synchronized (lock) {
				if (instance2nd == null) { // this check is must
					System.out.println("new 2nd");
					instance2nd = new AtomicInteger();
				}
			}
		}
		return instance2nd;
	}

	static AtomicInteger getInstance2ndV2() {
		if (instance2nd == null) {
			sync.lock();
			if (instance2nd == null) { // this check is must
				System.out.println("new 2ndV2");
				instance2nd = new AtomicInteger();
			}
			sync.unlock();
		}
		return instance2nd;
	}

	static int THREAD_COUNT = 1024;

	static class GetThread1 extends Thread {
		public void run() {
			AtomicInteger ai = getInstance1st();
			ai.getAndIncrement();
		}
	}

	static class GetThread2 extends Thread {
		public void run() {
			AtomicInteger ai = getInstance2nd();
			// AtomicInteger ai = getInstance2ndV2();
			ai.getAndIncrement();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// 我真无聊呀，这两方法没有明显对比性
		test2();
		test1();
	}

	static void test1() throws InterruptedException {
		Thread[] td = new Thread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			td[i] = new GetThread1();
		}
		long s = System.nanoTime();
		for (int i = 0; i < THREAD_COUNT; i++) {
			td[i].start();
		}
		for (int i = 0; i < THREAD_COUNT; i++) {
			td[i].join();
		}
		long t = System.nanoTime() - s;
		System.out.println("1st count:" + getInstance1st().get() + ", time:" + t);
	}

	static void test2() throws InterruptedException {
		Thread[] td = new Thread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			td[i] = new GetThread2();
		}
		long s = System.nanoTime();
		for (int i = 0; i < THREAD_COUNT; i++) {
			td[i].start();
		}
		for (int i = 0; i < THREAD_COUNT; i++) {
			td[i].join();
		}
		long t = System.nanoTime() - s;
		System.out.println("2nd count:" + getInstance2nd().get() + ", time:" + t);
	}
}
