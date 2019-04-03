package networking.contact.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Hashtable;
import java.util.Scanner;
import networking.contact.Constant;

public class ContactServer {
	
	/*��¼�����û���Զ��socket��ַ��Ϣ*/
	private static Hashtable<Integer, SocketAddress>	online_users		= new Hashtable<Integer, SocketAddress>();
	/*���������������, <id1, id2>: id1���󲦺ŵ�id2*/
	private static Hashtable<Integer, Integer>			conn_request_queue	= new Hashtable<Integer, Integer>();
	/*��¼�����û��ĸ��������˿���Ϣ*/
	private static Hashtable<Integer, Integer>			users_listen_port	= new Hashtable<Integer, Integer>();
	
	public static void main(String[] args) {
		startServer();
	}
	
	/**
	 * ��������
	 */
	public static void startServer() {
		try {
			// establish server socket
			
			ServerSocket ss = new ServerSocket(Constant.SERVER_LISTENING_PORT);
			
			int i = 1;
			while (true) {
				// wait for client connection
				Socket incoming = ss.accept();
				// local info
				System.out.println(incoming.getLocalSocketAddress());
				// remote info
				System.out.println(incoming.getRemoteSocketAddress());
				System.out.println("accept: " + i);
				online_users.put(i, incoming.getRemoteSocketAddress());
				ClientSocketProcessing csp = new ClientSocketProcessing(incoming, i);
				new Thread(csp).start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*������Ϣ�ַ���*/
	public static final String	help_info;
	static {
		StringBuffer helpInfoBuf = new StringBuffer(1024);
		helpInfoBuf.append("Enter " + Constant.CMD_GET_HELP + " to get help info\n");
		helpInfoBuf.append("Enter " + Constant.CMD_GET_ONLINE_USER + " to get all online users\n");
		helpInfoBuf.append("Enter " + Constant.CMD_GET_CONN_TO_2 + " to get connect to XXX\n");
		helpInfoBuf.append("Enter " + Constant.DISCONNECT_MESSAGE + " to exit\n");
		help_info = helpInfoBuf.toString();
	}
	
	/**
	 * һ��client��socket���ӵĴ����߳�
	 */
	private static class ClientSocketProcessing implements Runnable {
		
		private Socket	incoming;
		private int		id;
		
		public ClientSocketProcessing(Socket incoming, int id) {
			this.incoming = incoming;
			this.id = id;
		}
		
		public void run() {
			try {
				try {
					InputStream inStream = incoming.getInputStream();
					OutputStream outStream = incoming.getOutputStream();
					Scanner in = new Scanner(inStream);
					PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
					out.println(Constant.RES_SESSION_ID_HEADER + Constant.PORT_START + this.id + Constant.PORT_END);
					out.println("Hi " + this.id + "!\n" + help_info);
					// echo client input
					boolean keep = true;
					while (keep) {
						String line = in.nextLine();
						System.out.println("Client" + Constant.PORT_START + this.id + Constant.PORT_END + ":>" + line);
						out.println("Echo: " + line);
						if (Constant.DISCONNECT_MESSAGE.equalsIgnoreCase(line)) {
							keep = false;
						} else {
							parseCommand(line, out);
						}
						if (conn_request_queue.containsValue(this.id)) {
							// ��ǰ�û��������ˣ�����֪�����������˿�ʱ����Ҫѯ�����������˿�
							if (!users_listen_port.containsKey(this.id)) {
								out.println(Constant.CMD_WHAT_IS_YOUR_PORT);
							}
						} else if (conn_request_queue.containsKey(this.id)) {
							// ��ǰ�û��и������������Ȼ��Ŀ���û�id
							int destId = conn_request_queue.get(this.id);
							// �����ж�Ŀ���û��ڲ����ߣ��������ߣ�ɾ��Ŀ���û��������˿���Ϣ
							// ���Ѿ�֪��Ŀ���û��������˿ڣ����Ӧ��ǰ�û���������������������ɾ��������ȴ���ѯ��Ŀ���û����������˿�
							SocketAddress destAddress = online_users.get(destId);
							if (destAddress == null) {
								out.println(Constant.CONN_TO_2_RESULT_HEADER + "(250)" + Constant.PORT_START + destId
										+ Constant.PORT_END + "{Not Online!}");
								users_listen_port.remove(destId);// ����������Ϣ
							} else {
								if (users_listen_port.containsKey(destId)) {
									InetAddress ip = ((InetSocketAddress) destAddress).getAddress();
									int port = users_listen_port.get(destId);
									InetSocketAddress rightAddres = new InetSocketAddress(ip, port);
									out.println(Constant.CONN_TO_2_RESULT_HEADER + "(123)" + Constant.PORT_START + destId
											+ Constant.PORT_END + "{" + rightAddres + "}");
									// ɾ����ǰ�û�����������
									conn_request_queue.remove(this.id);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					incoming.close();
					online_users.remove(this.id);
					System.out.println("No." + this.id + " is close!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * ����client�η��ص��ַ�����������Ӧ�ַ���
		 * 
		 * @param line
		 * @param out
		 */
		private void parseCommand(String line, PrintWriter out) {
			String curCmd = line.trim();
			if (Constant.CMD_GET_HELP.equalsIgnoreCase(curCmd)) {
				out.println(help_info);
			} else if (Constant.CMD_GET_ONLINE_USER.equalsIgnoreCase(curCmd)) {
				String onlineUserStr = online_users.toString();
				out.println(Constant.ONLINE_USERS_HEADER + onlineUserStr);
			} else if (curCmd.toUpperCase().startsWith(Constant.CMD_GET_CONN_TO_2_PRE)) {
				int destCodeStart = Constant.CMD_GET_CONN_TO_2_PRE.length();
				int destCodeEnd = curCmd.indexOf(Constant.CMD_GET_CONN_TO_2_END, destCodeStart + 1);
				if (destCodeEnd < 0) {
					out.println(Constant.BAD_CMD_ERROR + " \"" + curCmd + "\"");
				} else {
					String destCode = curCmd.substring(destCodeStart, destCodeEnd);
					try {
						int destCodeInt = Integer.parseInt(destCode);
						if (destCodeInt == this.id) {
							out.println(Constant.CONN_TO_2_RESULT_HEADER + "(250)" + Constant.PORT_START + destCode
									+ Constant.PORT_END + "{Need not to connect to your self!}");
						} else {
							// �����ж�Ŀ���û��ڲ����ߣ�
							// �����ߣ���ȥ���Ŀ���û��������˿�
							// ���Ѿ�֪��Ŀ���û��������˿ڣ����Ӧ��ǰ�û����������������У���Ҫѯ��Ŀ���û����������˿�
							SocketAddress destAddress = online_users.get(destCodeInt);
							if (destAddress == null) {
								out.println(Constant.CONN_TO_2_RESULT_HEADER + "(250)" + Constant.PORT_START + destCodeInt
										+ Constant.PORT_END + "{Not Online!}");
								users_listen_port.remove(destCodeInt);// ����������Ϣ
							} else {
								if (users_listen_port.containsKey(destCodeInt)) {
									InetAddress ip = ((InetSocketAddress) destAddress).getAddress();
									int port = users_listen_port.get(destCodeInt);
									InetSocketAddress rightAddres = new InetSocketAddress(ip, port);
									out.println(Constant.CONN_TO_2_RESULT_HEADER + "(123)" + Constant.PORT_START + destCodeInt
											+ Constant.PORT_END + "{" + rightAddres + "}");
								} else {
									// this.id���󲦺�destCodeInt�������������
									conn_request_queue.put(this.id, destCodeInt);
								}
							}
						}
					} catch (Exception e) {
						out.println(Constant.BAD_CMD_ERROR + " \"" + curCmd + "\"");
					}
				}
			} else if (Constant.CMD_WHAT_IS_SESSION_ID.equalsIgnoreCase(curCmd)) {
				// client����ǰsession id
				out.println(Constant.RES_SESSION_ID_HEADER + Constant.PORT_START + this.id + Constant.PORT_END);
			} else if (line.startsWith(Constant.RES_LISTEN_PORT_HEADER)) {
				// ����client���صģ����������˿���Ϣ�ַ���
				int posOfEnd = line.indexOf(Constant.PORT_END, Constant.RES_LISTEN_PORT_HEADER.length() + 2);
				String listenPortStr = line.substring(Constant.RES_LISTEN_PORT_HEADER.length() + 1, posOfEnd);
				System.out.println(listenPortStr);
				try {
					int listenPortInt = Integer.parseInt(listenPortStr);
					users_listen_port.put(this.id, listenPortInt);
				} catch (Exception e) {}
			}
		}
	}
}
