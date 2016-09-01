package thread;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * a simple demo of task pool with multi priority supported.
 * <p>
 * priority ranges from 1 to 10, the higher it is, the earlier the task executes.
 * 
 */
public class MultiPriorityTaskTest {

	// the task queue
	static final int PRIORITY_NUM = 10;
	ConcurrentLinkedDeque<Task>[] taskQueue;
	// the task thread, it executes tasks
	static final int WORK_THREAD_NUM = 3;
	Thread[] taskThread;
	transient boolean running = false;

	int count;

	public MultiPriorityTaskTest() {
		// init task queue
		taskQueue = new ConcurrentLinkedDeque[PRIORITY_NUM];
		for (int i = 0; i < PRIORITY_NUM; i++) {
			taskQueue[i] = new ConcurrentLinkedDeque<Task>();
		}
		// inits task thread
		taskThread = new Thread[WORK_THREAD_NUM];
		for (int i = 0; i < WORK_THREAD_NUM; i++) {
			taskThread[i] = new Thread() {
				public void run() {
					while (running) {
						// just sleep awhile
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// carry on tasks, however, executing only one task is all right, to continue to run another.
						boolean notask = true;
						for (int k = PRIORITY_NUM - 1; k >= 0; k--) {
							if (!taskQueue[k].isEmpty()) {
								Task task = taskQueue[k].poll();
								if (task == null) {
									continue;
								}
								task.execute();
								count++;
								notask = false;
								break;// only one task executing is OK!
							}
						}
						// check whether no task executes or not
						if (notask) {
							synchronized (taskQueue) {
								try {
									taskQueue.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			};
		}
	}

	/**
	 * 
	 * @param task
	 * @param priority
	 *            1 to 10, the higher it is, the earlier the task executes.
	 */
	void addTask(Task task, int priority) {
		if (priority < 1) {
			priority = 1;
		}
		if (priority > PRIORITY_NUM) {
			priority = PRIORITY_NUM;
		}
		priority--;
		synchronized (taskQueue) {
			taskQueue[priority].add(task);
			taskQueue.notifyAll();// to notify taskThread not to wait
		}
	}

	void start() {
		count = 0;
		running = true;
		for (int i = 0; i < WORK_THREAD_NUM; i++) {
			taskThread[i].start();
		}
	}

	void stop() {
		running = false;
		synchronized (taskQueue) {
			taskQueue.notifyAll();// to notify taskThread not to wait
		}
		// taskThread.stop();
		System.out.println("Totally, " + count + " tasks is executed.");
	}

	// define a task API
	interface Task {
		void execute();
	}

	// define a real task
	class MyTask implements Task {
		String name;
		int priority;

		public MyTask(String name, int priority) {
			this.name = name;
			this.priority = priority;
		}

		public void execute() {
			System.out.println("I am " + name + ", my level is " + priority);
		}

	}

	public static void main(String[] args) {
		MultiPriorityTaskTest mptt = new MultiPriorityTaskTest();
		mptt.start();

		for (int i = 0; i < 1000000; i++) {
			int priority = (int) (Math.random() * 10) + 1;
			Task task = mptt.new MyTask("task" + i, priority);
			mptt.addTask(task, priority);
		}

		try {
			Thread.sleep(1000 * 60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mptt.stop();
	}

}
