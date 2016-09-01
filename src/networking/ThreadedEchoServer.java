package networking;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This program implements a multithreaded server that listens to port 8189 and echoes back all client input.
 * 
 * @author Cay Horstmann
 * @version 1.20 2004-08-03
 */
public class ThreadedEchoServer {
	public static void main(String[] args) {
		try {
			int i = 1;
			ServerSocket s = new ServerSocket(8189);

			while (true) {
				Socket incoming = s.accept();
				System.out.println("Spawning " + i);
				// remote info
				System.out.println(incoming.getPort());
				System.out.println(incoming.getInetAddress());
				System.out.println(incoming.getRemoteSocketAddress());
				
				//local info
				System.out.println(incoming.getLocalPort());
				System.out.println(incoming.getLocalAddress());
				System.out.println(incoming.getLocalSocketAddress());
				Runnable r = new ThreadedEchoHandler(incoming);
				new Thread(r).start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * This class handles the client input for one server socket connection.
 */
class ThreadedEchoHandler implements Runnable {
	/**
	 * Constructs a handler.
	 * 
	 * @param i
	 *            the incoming socket
	 * @param c
	 *            the counter for the handlers (used in prompts)
	 */
	public ThreadedEchoHandler(Socket i) {
		incoming = i;
	}

	public void run() {
		try {
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

				out.println("Hello! Enter BYE to exit.");

				// echo client input
				boolean done = false;
				while (!done) {
					String line = in.nextLine();
					System.out.println("get:" + line);
					out.println("Echo: " + line);

					if (line.trim().equals("BYE")) {
						done = true;
					}
				}
			} finally {
				incoming.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Socket incoming;
}
