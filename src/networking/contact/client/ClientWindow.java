package networking.contact.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import networking.contact.Constant;

public class ClientWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private boolean connect_flag = false;
	private boolean want_to_dial = false;
	private int listen_port = -1;
	/* 服务器分配的session id */
	private volatile int my_id = -1;
	private Socket skToServer;
	private PrintWriter pwToServer;
	private Scanner inFromServer;

	/* for chat dialog */
	private volatile int my_friend_id = -1;
	private static volatile boolean isChating = false;
	private static volatile Object chatingLock = new Object();
	private static Hashtable<Integer, SocketAddress> friends_addresses = new Hashtable<Integer, SocketAddress>();

	private JTextArea messageShowArea;
	private JTextArea messageInputArea;

	public static void main(String[] args) {
		ClientWindow cw = new ClientWindow();
		cw.setVisible(true);
		cw.startConnect();
		cw.startListen();
	}

	public ClientWindow() {
		super();
		setTitle("ClientWindow");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		messageShowArea = new JTextArea();
		messageShowArea.setTabSize(4);
		messageShowArea.setEditable(false);
		JScrollPane sp = new JScrollPane(messageShowArea);
		add(sp);

		messageInputArea = new JTextArea();
		messageInputArea.setTabSize(4);
		messageInputArea.setToolTipText("Press 'Enter' key to send message!");
		JScrollPane spInput = new JScrollPane(messageInputArea);
		add(spInput, BorderLayout.SOUTH);
		messageInputArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int curKey = e.getKeyCode();
				if (KeyEvent.VK_ENTER == curKey) {
					String inputLine = messageInputArea.getText();
					// 剔除换行符
					int ill = inputLine.length();
					inputLine = inputLine.substring(0, ill - 1);
					if ((ill > 1) && (inputLine.charAt(ill - 2) == '\r')) {
						inputLine = inputLine.substring(0, ill - 2);
					}
					if ("".equals(inputLine.trim())) {
						return;
					}
					try {
						// 发送到server端
						pwToServer.println(inputLine);
						if (inputLine.toUpperCase().startsWith(Constant.CMD_GET_CONN_TO_2_PRE)) {
							want_to_dial = true;
						}
						messageShowArea.append(my_id + "@Server>" + inputLine + "\n");
						messageInputArea.setText("");
					} catch (Exception ex) {
						messageShowArea.append(my_id + "@Server>" + inputLine + "\n");
						messageShowArea.append(ex.getMessage() + "\n");
					}
				}
			}
		});

		centerOnScreen();
	}

	private void centerOnScreen() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		this.setSize(screenWidth / 3, screenHeight / 3);
		this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
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
				} catch (Exception e) {
				}
			} while (!successFlag);
			while (true) {
				// wait for some one to connect
				Socket incoming = ss.accept();
				try {
					new ChatDialog(this, incoming, my_id, -1, false);
					isChating = true;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void oldDealFriendContact(Socket incoming) {
		System.out.println("I am " + my_id + " is called...");
		// local info
		System.out.println("My address:" + incoming.getLocalSocketAddress());
		// remote info
		System.out.println("Friend's address" + incoming.getRemoteSocketAddress());
		try {
			InputStream inStream = incoming.getInputStream();
			OutputStream outStream = incoming.getOutputStream();

			Scanner in = new Scanner(inStream);
			PrintWriter out = new PrintWriter(outStream, true);// autoFlush

			out.println("Hello! I am " + this.my_id);

			// echo client input
			boolean keep = true;
			while (keep) {
				String line = in.nextLine();
				parseMessageFromFriend(line, out);
				messageShowArea.append("friend" + Constant.PORT_START + my_friend_id + Constant.PORT_END + ":>" + line
						+ "\n");
				if (my_friend_id <= 0) {
					out.println(Constant.CMD_WHAT_IS_YOUR_ID);
					continue;
				} else {
					break;
				}
				// out.println("Echo(from " + this.my_id + "): " + line);
				// if (Constant.DISCONNECT_MESSAGE.equalsIgnoreCase(line.trim())) {
				// keep = false;
				// }
			}
			new ChatDialog(this, incoming, my_id, -1, false);
			isChating = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// incoming.close();
			// isChating = false;
		}
	}

	/**
	 * 解析对等Client返回的应答
	 * 
	 * @param line
	 */
	private void parseMessageFromFriend(String line, PrintWriter out) {
		if (this.my_friend_id <= 0) {
			// 解析friend发来的ID
			if (line.startsWith(Constant.RES_MY_ID_HEADER)) {
				int posOfEnd = line.indexOf(Constant.PORT_END, Constant.RES_MY_ID_HEADER.length() + 2);
				String idStr = line.substring(Constant.RES_MY_ID_HEADER.length() + 1, posOfEnd);
				System.out.println("Friend's id is " + idStr);
				try {
					this.my_friend_id = Integer.parseInt(idStr);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 启动链接服务器
	 */
	public void startConnect() {
		try {
			skToServer = new Socket("localhost", Constant.SERVER_LISTENING_PORT);
			connect_flag = true;
			// local info
			System.out.println("I am online, at address:" + skToServer.getLocalSocketAddress());
			// remote info
			System.out.println("Server address:" + skToServer.getRemoteSocketAddress());

			try {
				pwToServer = new PrintWriter(skToServer.getOutputStream(), true);
				inFromServer = new Scanner(skToServer.getInputStream());

				Thread readThread = new Thread(new Runnable() {
					public void run() {
						while (connect_flag) {
							try {
								String line = inFromServer.nextLine();
								messageShowArea.append("Server:>" + line + "\n");
								parseReceiveMessage(line, inFromServer, pwToServer);
							} catch (Exception e) {
								e.printStackTrace();
								try {
									skToServer.close();
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								connect_flag = false;
							}
						}
					}
				});

				readThread.start();

				while (my_id <= 0) {
					pwToServer.println(Constant.CMD_WHAT_IS_SESSION_ID);
					Thread.sleep(100);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				skToServer.close();
				connect_flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			connect_flag = false;
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
				String socketAddressStr = line.substring(posOfFlashChar);
				// System.out.println(socketAddress);

				int posOfColon = socketAddressStr.indexOf(':');
				String ip = socketAddressStr.substring(2, posOfColon);
				int posOfAddressEnd = socketAddressStr.indexOf('}', posOfColon + 1);
				String port = socketAddressStr.substring(posOfColon + 1, posOfAddressEnd);

				InetSocketAddress friendSkAddr = new InetSocketAddress(ip, Integer.valueOf(port));
				// System.out.println(destSkAddr);

				try {
					Socket newSk = new Socket(friendSkAddr.getAddress(), friendSkAddr.getPort());
					friends_addresses.put(friendId, friendSkAddr);
					new ChatDialog(this, newSk, my_id, friendId, true);
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
				System.out.println("My id is:" + idStr);
				try {
					this.my_id = Integer.parseInt(idStr);
				} catch (Exception e) {
				}
			}
		}
		if (Constant.CMD_WHAT_IS_YOUR_PORT.equalsIgnoreCase(line)) {
			// 服务器请求自己的侦听端口
			pw.println(Constant.RES_LISTEN_PORT_HEADER + Constant.PORT_START + this.listen_port + Constant.PORT_END);
		}
	}

}
