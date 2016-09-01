package thread;

class Robot extends Thread {
	private static byte[] commands;
	private RobotController controller;

	public Robot(RobotController c, String name) {
		controller = c;
		this.setName(name);
	}

	public static void storeCommands(byte[] b) {
		commands = b;
	}

	public void processCommand(byte[] b) {
		try {
			int size = b.length;
			for (int i = 0; i < size; i++) {
				System.out.println(b[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} // Exception is ignored
	}

	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName()+":run");
			synchronized (controller) { // 1
				System.out.println(Thread.currentThread().getName()+":enter");
				while (commands == null) { // 2
					// if (commands == null){ //2
					try {
						controller.wait(); // 3
					} catch (InterruptedException e) {
						e.printStackTrace();
					} // Exception is ignored
				} // purposefully.
					// Now we have commands for the robot.
				System.out.println(Thread.currentThread().getName() + ":go");
				processCommand(commands); // Move the robot.
				commands = null;
			}
		}
	}
}

class RobotController extends Thread {
	private Robot robot1;
	private Robot robot2;

	public static void main(String args[]) {
		RobotController rc = new RobotController();
		rc.start();
	}

	public void run() {
		robot1 = new Robot(this, "Robot" + 1);
		robot2 = new Robot(this, "Robot" + 2);
		robot1.start();
		robot2.start();
		try{
			Thread.sleep(1000);
		}catch (Exception e) {
			// TODO: handle exception
		}
		loadCommands(new byte[] { 12, 32 });
	}

	public void loadCommands(byte[] b) {
		synchronized (this) { // 4
			Robot.storeCommands(b); // Give the commands to the Robot
			notifyAll(); // class. Notify all threads.

		}
	}
}