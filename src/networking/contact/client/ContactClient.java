package networking.contact.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import networking.contact.Constant;

public class ContactClient {
	
	private boolean					connect_flag	= false;
	private Thread					readThread		= null;
	private Thread					writeThread		= null;
	
	private boolean					want_to_dial	= false;
	private int						listen_port		= -1;
	/*服务器分配的session id*/
	private volatile int			my_id			= -1;
	private volatile int			my_friend_id	= -1;
	private static volatile boolean	isChating		= false;
	private static volatile Object	chatingLock		= new Object();
	
	public static void main(String[] args) {
		ContactClient cc = new ContactClient();
		cc.startConnect();
		cc.startListen();
	}
	
	/**
	 * 启动自己的侦听线程
	 */
	public void startListen() {
		try {
			// establish client listening socket
			ServerSocket ss = null;
			boolean successFlag = false;
			do {
				try {
					listen_port = Constant.CLIENT_LISTENING_PORT + new Random().nextInt(1024);
					ss = new ServerSocket(listen_port);
					successFlag = true;
				} catch (Exception e) {}
			} while (!successFlag);
			while (true) {
				// wait for some one to connect
				Socket incoming = ss.accept();
				try {
					InputStream inStream = incoming.getInputStream();
					OutputStream outStream = incoming.getOutputStream();
					
					Scanner in = new Scanner(inStream);
					PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
					
					out.println("Hello! I am " + this.my_id);
					
					// echo client input
					boolean keep = true;
					while (keep) {
						String line = in.nextLine();
						parseMessageFromFriend(line, out);
						if (my_friend_id <= 0) {
							out.println(Constant.CMD_WHAT_IS_YOUR_ID);
							continue;
						}
						System.out.println("friend" + Constant.PORT_START + my_friend_id + Constant.PORT_END + ":>" + line);
						out.println("Echo(from " + this.my_id + "): " + line);
						if (Constant.DISCONNECT_MESSAGE.equalsIgnoreCase(line.trim())) {
							keep = false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					incoming.close();
					isChating = false;
					synchronized (chatingLock) {
						try {
							chatingLock.notifyAll();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析对等Client返回的应答
	 * 
	 * @param line
	 */
	private void parseMessageFromFriend(String line, PrintWriter out) {
		if (this.my_friend_id <= 0) {
			// 请求服务器给自己分配的session id
			if (line.startsWith(Constant.RES_MY_ID_HEADER)) {
				int posOfEnd = line.indexOf(Constant.PORT_END, Constant.RES_MY_ID_HEADER.length() + 2);
				String idStr = line.substring(Constant.RES_MY_ID_HEADER.length() + 1, posOfEnd);
				System.out.println(idStr);
				try {
					this.my_friend_id = Integer.parseInt(idStr);
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * 启动链接服务器
	 */
	public void startConnect() {
		try {
			final Socket sk = new Socket("localhost", Constant.SERVER_LISTENING_PORT);
			connect_flag = true;
			// local info
			System.out.println(sk.getLocalSocketAddress());
			// remote info
			System.out.println(sk.getRemoteSocketAddress());
			
			try {
				final PrintWriter pw = new PrintWriter(sk.getOutputStream(), true);
				final Scanner in = new Scanner(sk.getInputStream());
				
				readThread = new Thread(new Runnable() {
					public void run() {
						while (connect_flag) {
							try {
								String line = in.nextLine();
								System.out.println("Server:>" + line);
								parseReceiveMessage(line, in, pw);
							} catch (Exception e) {
								e.printStackTrace();
								try {
									sk.close();
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								connect_flag = false;
							}
						}
					}
				});
				
				writeThread = new Thread(new Runnable() {
					public void run() {
						Scanner read = new Scanner(System.in);
						while (connect_flag) {
							synchronized (chatingLock) {
								while (isChating) {
									try {
										chatingLock.wait();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							try {
								if (my_id <= 0) {
									pw.println(Constant.CMD_WHAT_IS_SESSION_ID);
									continue;
								}
//								ioWait();
								System.out.print(my_id + "@Server>");
								String line = read.nextLine();
//								ioSignal();
								if ("".equals(line.trim())) {
									continue;
								}
								pw.println(line);
								if (!isChating && line.toUpperCase().startsWith(Constant.CMD_GET_CONN_TO_2_PRE)) {
									want_to_dial = true;
								}
							} catch (Exception e) {
								e.printStackTrace();
								try {
									sk.close();
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								connect_flag = false;
							}
						}
					}
				});
				
				readThread.start();
				writeThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				sk.close();
				// if (readThread != null && readThread.isAlive()) {
				// readThread.join(100);
				// readThread = null;
				// }
				// if (writeThread != null && writeThread.isAlive()) {
				// writeThread.join(100);
				// writeThread = null;
				// }
				connect_flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			connect_flag = false;
			readThread = null;
			writeThread = null;
		}
	}
	
	/**
	 * 解析服务器返回的应答
	 * 
	 * @param line
	 * @param in
	 * @param pw
	 */
	private void parseReceiveMessage(String line, Scanner in, PrintWriter pw) {
		if (want_to_dial) {
			String okResponseHeader = "#CONN_REQ_RES:(123)" + Constant.PORT_START;
			if (line.startsWith(okResponseHeader)) {
				int posOfPortEnd = line.indexOf(Constant.PORT_END, okResponseHeader.length() + 1);
				String friendIdStr = line.substring(okResponseHeader.length(), posOfPortEnd);
				int friendId = Integer.valueOf(friendIdStr);
				
				int posOfFlashChar = line.indexOf('{', posOfPortEnd + 1);
				String socketAddress = line.substring(posOfFlashChar);
				// System.out.println(socketAddress);
				
				int posOfColon = socketAddress.indexOf(':');
				String ip = socketAddress.substring(2, posOfColon);
				int posOfAddressEnd = socketAddress.indexOf('}', posOfColon + 1);
				String port = socketAddress.substring(posOfColon + 1, posOfAddressEnd);
				
				InetSocketAddress destSkAddr = new InetSocketAddress(ip, Integer.valueOf(port));
				// System.out.println(destSkAddr);
				
				try {
					Socket newSk = new Socket(destSkAddr.getAddress(), destSkAddr.getPort());
					ConnectToFriendThread ctft = new ConnectToFriendThread(newSk, friendId, my_id);
					new Thread(ctft).start();
					isChating = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (line.startsWith("#CONN_REQ_RES:(250)" + Constant.PORT_START)) {
				want_to_dial = false;
			}
		}
		if (this.my_id <= 0) {
			// 请求服务器给自己分配的session id
			if (line.startsWith(Constant.RES_SESSION_ID_HEADER)) {
				int posOfEnd = line.indexOf(Constant.PORT_END, Constant.RES_SESSION_ID_HEADER.length() + 2);
				String idStr = line.substring(Constant.RES_SESSION_ID_HEADER.length() + 1, posOfEnd);
				System.out.println(idStr);
				try {
					this.my_id = Integer.parseInt(idStr);
				} catch (Exception e) {}
			}
		}
		if (Constant.CMD_WHAT_IS_YOUR_PORT.equalsIgnoreCase(line)) {
			// 服务器请求自己的侦听端口
			pw.println(Constant.RES_LISTEN_PORT_HEADER + Constant.PORT_START + this.listen_port + Constant.PORT_END);
		}
	}
	
	private static volatile boolean	ioOccupyFlag	= false;
	private static volatile Object	ioMutexLock		= new Object();
	
	private static void ioWait() {
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
	
	private static void ioSignal() {
		ioOccupyFlag = false;
		synchronized (ioMutexLock) {
			try {
				ioMutexLock.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 一个单独的回话
	 */
	private static class ConnectToFriendThread implements Runnable {
		
		private Socket	sk;
		private int		friend_id;
		private int		myself_id;
		
		public ConnectToFriendThread(Socket sk, int friend_id, int my_id) {
			this.sk = sk;
			this.friend_id = friend_id;
			this.myself_id = my_id;
		}
		
		public void run() {
			// local info
			System.out.println("MyAddress:" + sk.getLocalSocketAddress());
			
			// remote info
			System.out.println("FriendAddress:" + sk.getRemoteSocketAddress());
			try {
				final PrintWriter pw = new PrintWriter(sk.getOutputStream(), true);
				final Scanner in = new Scanner(sk.getInputStream());
				
				// 首先报告自己的ID
				pw.println(Constant.RES_MY_ID_HEADER + Constant.PORT_START + this.myself_id + Constant.PORT_END);
				
				Thread readThread = new Thread(new Runnable() {
					public void run() {
						try {
							boolean keep = true;
							while (keep) {
								String line = in.nextLine();
								System.out
										.println("Friend" + Constant.PORT_START + friend_id + Constant.PORT_END + ":>" + line);
								parseMessageFromFriend(line, in, pw);
								if (Constant.DISCONNECT_MESSAGE.equalsIgnoreCase(line.trim())) {
									keep = false;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							try {
								sk.close();
								isChating = false;
								synchronized (chatingLock) {
									try {
										chatingLock.notifyAll();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							} catch (IOException e2) {
								e2.printStackTrace();
							}
						}
					}
				});
				
				Thread writeThread = new Thread(new Runnable() {
					public void run() {
						Scanner read = new Scanner(System.in);
						try {
							while (true) {
//								ioWait();
								System.out.print("To" + Constant.PORT_START + friend_id + Constant.PORT_END + ">");
								String line = read.nextLine();
//								ioSignal();
								if ("".equals(line.trim())) {
									continue;
								}
								// String line = "" + Math.random();
								pw.println(line);
							}
						} catch (Exception e) {
							e.printStackTrace();
							try {
								sk.close();
								isChating = false;
								synchronized (chatingLock) {
									try {
										chatingLock.notifyAll();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							} catch (IOException e2) {
								e2.printStackTrace();
							}
						}
					}
				});
				
				readThread.start();
				writeThread.start();
				
			} catch (Exception e) {
				e.printStackTrace();
				try {
					sk.close();
					isChating = false;
					synchronized (chatingLock) {
						try {
							chatingLock.notifyAll();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {}
		}
		
		private void parseMessageFromFriend(String line, Scanner in, PrintWriter pw) {
			if (Constant.CMD_WHAT_IS_YOUR_ID.equalsIgnoreCase(line)) {
				// 朋友请求自己的ID
				pw.println(Constant.RES_MY_ID_HEADER + Constant.PORT_START + this.myself_id + Constant.PORT_END);
			}
		}
	}
}
