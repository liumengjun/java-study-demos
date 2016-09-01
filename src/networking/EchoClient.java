package networking;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	public static volatile boolean ioOccupyFlag = false;
	public static volatile Object ioMutexLock = new Object();

	public static void main(String[] args) throws Exception {
		Socket sk = new Socket("localhost", 8189);
		
		// local info
		System.out.println(sk.getLocalPort());
		System.out.println(sk.getLocalAddress());
		System.out.println(sk.getLocalSocketAddress());
		
//remote info
//		System.out.println(sk.getPort());
//		System.out.println(sk.getInetAddress());
		System.out.println(sk.getRemoteSocketAddress());
		try {
			final PrintWriter pw = new PrintWriter(sk.getOutputStream(), true);
			final Scanner in = new Scanner(sk.getInputStream());

			Thread readThread = new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						String line = in.nextLine();
						ioWait();
						System.out.println(line);
						ioSignal();
					}
				}
			});

			Thread writeThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Scanner read = new Scanner(System.in);
					while (true) {
						ioWait();
						System.out.print('>');
						String line = read.nextLine();
						ioSignal();
						if ("".equals(line.trim())) {
							continue;
						}
						// String line = "" + Math.random();
						pw.println(line);
					}
				}
			});

			readThread.start();
			writeThread.start();

		} catch (Exception e) {
			e.printStackTrace();
			sk.close();
		} finally {
		}
	}

	public static void ioWait() {
		synchronized (ioMutexLock) {
			while (ioOccupyFlag) {
				try {
					ioMutexLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		ioOccupyFlag = true;
	}

	public static void ioSignal() {
		ioOccupyFlag = false;
		synchronized (ioMutexLock) {
			try {
				ioMutexLock.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
