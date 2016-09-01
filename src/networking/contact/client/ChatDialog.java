package networking.contact.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import networking.contact.Constant;

public class ChatDialog extends JDialog {

	private static final long serialVersionUID = -3427521040494463680L;

	private Socket socket2Friend;
	private int myself_id;
	private int friend_id;
	private boolean startByMeFlag;

	private PrintWriter pwToFriend;
	private Scanner inFromFriend;

	private JTextArea chatMessageShowArea;
	private JTextArea chatMessageInputArea;

	public ChatDialog(ClientWindow parent, Socket sk, int myself_id, int friend_id, boolean startByMeFlag)
			throws IOException {
		super(parent, false);
		this.socket2Friend = sk;
		this.myself_id = myself_id;
		this.friend_id = friend_id;
		this.startByMeFlag = startByMeFlag;
		updateTitle();
		pwToFriend = new PrintWriter(sk.getOutputStream(), true);
		inFromFriend = new Scanner(sk.getInputStream());

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					pwToFriend.close();
					inFromFriend.close();
					socket2Friend.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				ChatDialog.this.dispose();
			}
		});

		chatMessageShowArea = new JTextArea();
		chatMessageShowArea.setTabSize(4);
		chatMessageShowArea.setEditable(false);
		JScrollPane sp = new JScrollPane(chatMessageShowArea);
		add(sp);

		chatMessageInputArea = new JTextArea();
		chatMessageInputArea.setTabSize(4);
		chatMessageInputArea.setToolTipText("Press 'Enter' key to send message!");
		JScrollPane spInput = new JScrollPane(chatMessageInputArea);
		add(spInput, BorderLayout.SOUTH);
		chatMessageInputArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int curKey = e.getKeyCode();
				if (KeyEvent.VK_ENTER == curKey) {
					if (socket2Friend.isClosed()) {
						chatMessageShowArea.append("Connection is close!\n");
						return;
					}
					String inputLine = chatMessageInputArea.getText();
					// 剔除换行符
					int ill = inputLine.length();
					inputLine = inputLine.substring(0, ill - 1);
					if (inputLine.charAt(ill - 2) == '\r') {
						inputLine = inputLine.substring(0, ill - 2);
					}
					if ("".equals(inputLine.trim())) {
						return;
					}
					try {
						pwToFriend.println(inputLine);
						chatMessageShowArea.append("To" + Constant.PORT_START + ChatDialog.this.friend_id
								+ Constant.PORT_END + ">");
						chatMessageShowArea.append(inputLine + "\n");
						chatMessageInputArea.setText("");
					} catch (Exception ex) {
						chatMessageShowArea.append("To" + Constant.PORT_START + ChatDialog.this.friend_id
								+ Constant.PORT_END + ">");
						chatMessageShowArea.append(inputLine + "\n");
						chatMessageShowArea.append(ex.getMessage() + "\n");
					}
				}
			}
		});

		this.setSize(300, 200);
		centerOnScreen(parent);
		this.setVisible(true);

		ConnectToFriendThread ctft = new ConnectToFriendThread(sk, friend_id, myself_id);
		new Thread(ctft).start();
	}

	private void updateTitle() {
		String title = "I am " + Constant.PORT_START + myself_id + Constant.PORT_END + ", ";
		if (startByMeFlag) {
			title += "Chatting with " + Constant.PORT_START + friend_id + Constant.PORT_END;
		} else {
			title += "Chatting from " + Constant.PORT_START + friend_id + Constant.PORT_END;
		}
		this.setTitle(title);
	}

	private void centerOnScreen(Window window) {
		Point windowLoc = window.getLocation();
		Dimension windowSize = window.getSize();
		int windowHeight = windowSize.height;
		int windowWidth = windowSize.width;
		int destX = windowLoc.x + (windowWidth - this.getWidth()) / 2;
		int destY = windowLoc.y + (windowHeight - this.getHeight()) / 2;
		this.setLocation(destX, destY);
	}

	/**
	 * 一个单独的chat的处理线程
	 */
	private class ConnectToFriendThread implements Runnable {
		private Socket sk;
		// private int friend_id;
		private int myself_id;

		public ConnectToFriendThread(Socket sk, int friend_id, int my_id) {
			this.sk = sk;
			// this.friend_id = friend_id;
			this.myself_id = my_id;
		}

		public void run() {
			System.out.println("I am " + this.myself_id + " is going to chat...");
			// local info
			System.out.println("MyAddress:" + sk.getLocalSocketAddress());
			// remote info
			System.out.println("FriendAddress:" + sk.getRemoteSocketAddress());
			try {
				if (startByMeFlag) {
					// 首先报告自己的ID
					pwToFriend.println(Constant.RES_MY_ID_HEADER + Constant.PORT_START + this.myself_id
							+ Constant.PORT_END);
				}
				pwToFriend.println("Hello! I am " + this.myself_id);

				Thread readThread = new Thread(new Runnable() {
					public void run() {
						try {
							boolean keep = true;
							while (keep && ChatDialog.this.isVisible()) {
								String line = inFromFriend.nextLine();
								chatMessageShowArea.append("Friend" + Constant.PORT_START + friend_id
										+ Constant.PORT_END + ":>" + line + "\n");
								parseMessageFromFriend(line, inFromFriend, pwToFriend);
								if (Constant.DISCONNECT_MESSAGE.equalsIgnoreCase(line.trim())) {
									keep = false;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							chatMessageShowArea.append("Friend" + Constant.PORT_START + friend_id + Constant.PORT_END
									+ " is quit, ");
							chatMessageShowArea.append("Chating is over!\n");
							try {
								sk.close();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
						}
					}
				});

				readThread.start();
				if (!startByMeFlag) {
					while (friend_id <= 0) {
						pwToFriend.println(Constant.CMD_WHAT_IS_YOUR_ID);
						Thread.sleep(100);
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					sk.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
			}
		}

		private void parseMessageFromFriend(String line, Scanner in, PrintWriter pw) {
			if (startByMeFlag) {
				if (Constant.CMD_WHAT_IS_YOUR_ID.equalsIgnoreCase(line)) {
					// 朋友请求自己的ID
					pw.println(Constant.RES_MY_ID_HEADER + Constant.PORT_START + this.myself_id + Constant.PORT_END);
				}
			} else {
				if (friend_id <= 0) {
					// 解析friend发来的ID
					if (line.startsWith(Constant.RES_MY_ID_HEADER)) {
						int posOfEnd = line.indexOf(Constant.PORT_END, Constant.RES_MY_ID_HEADER.length() + 2);
						String idStr = line.substring(Constant.RES_MY_ID_HEADER.length() + 1, posOfEnd);
						System.out.println("Friend's id is " + idStr);
						try {
							friend_id = Integer.parseInt(idStr);
							updateTitle();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

}
