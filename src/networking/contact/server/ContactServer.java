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
	
	/*记录在线用户的远端socket地址信息*/
	private static Hashtable<Integer, SocketAddress>	online_users		= new Hashtable<Integer, SocketAddress>();
	/*链接请求任务队列, <id1, id2>: id1请求拨号到id2*/
	private static Hashtable<Integer, Integer>			conn_request_queue	= new Hashtable<Integer, Integer>();
	/*记录在线用户的各自侦听端口信息*/
	private static Hashtable<Integer, Integer>			users_listen_port	= new Hashtable<Integer, Integer>();
	
	public static void main(String[] args) {
		startServer();
	}
	
	/**
	 * 启动服务
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
	
	/*帮组信息字符串*/
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
	 * 一个client的socket链接的处理线程
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
							// 当前用户被拨号了，当不知道它的侦听端口时，需要询问它的侦听端口
							if (!users_listen_port.containsKey(this.id)) {
								out.println(Constant.CMD_WHAT_IS_YOUR_PORT);
							}
						} else if (conn_request_queue.containsKey(this.id)) {
							// 当前用户有个拨号请求，首先获得目的用户id
							int destId = conn_request_queue.get(this.id);
							// 首先判断目的用户在不在线，若不在线，删除目的用户的侦听端口信息
							// 若已经知道目的用户的侦听端口，则回应当前用户，并将该请求从任务队列删除；否则等待，询问目的用户它的侦听端口
							SocketAddress destAddress = online_users.get(destId);
							if (destAddress == null) {
								out.println(Constant.CONN_TO_2_RESULT_HEADER + "(250)" + Constant.PORT_START + destId
										+ Constant.PORT_END + "{Not Online!}");
								users_listen_port.remove(destId);// 清除错误的信息
							} else {
								if (users_listen_port.containsKey(destId)) {
									InetAddress ip = ((InetSocketAddress) destAddress).getAddress();
									int port = users_listen_port.get(destId);
									InetSocketAddress rightAddres = new InetSocketAddress(ip, port);
									out.println(Constant.CONN_TO_2_RESULT_HEADER + "(123)" + Constant.PORT_START + destId
											+ Constant.PORT_END + "{" + rightAddres + "}");
									// 删除当前用户的请求任务
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
		 * 解析client段返回的字符串，命令，或回应字符串
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
							// 首先判断目的用户在不在线，
							// 若在线，再去获得目的用户的侦听端口
							// 若已经知道目的用户的侦听端口，则回应当前用户；否则加入任务队列，需要询问目的用户它的侦听端口
							SocketAddress destAddress = online_users.get(destCodeInt);
							if (destAddress == null) {
								out.println(Constant.CONN_TO_2_RESULT_HEADER + "(250)" + Constant.PORT_START + destCodeInt
										+ Constant.PORT_END + "{Not Online!}");
								users_listen_port.remove(destCodeInt);// 清除错误的信息
							} else {
								if (users_listen_port.containsKey(destCodeInt)) {
									InetAddress ip = ((InetSocketAddress) destAddress).getAddress();
									int port = users_listen_port.get(destCodeInt);
									InetSocketAddress rightAddres = new InetSocketAddress(ip, port);
									out.println(Constant.CONN_TO_2_RESULT_HEADER + "(123)" + Constant.PORT_START + destCodeInt
											+ Constant.PORT_END + "{" + rightAddres + "}");
								} else {
									// this.id请求拨号destCodeInt，加入任务队列
									conn_request_queue.put(this.id, destCodeInt);
								}
							}
						}
					} catch (Exception e) {
						out.println(Constant.BAD_CMD_ERROR + " \"" + curCmd + "\"");
					}
				}
			} else if (Constant.CMD_WHAT_IS_SESSION_ID.equalsIgnoreCase(curCmd)) {
				// client请求当前session id
				out.println(Constant.RES_SESSION_ID_HEADER + Constant.PORT_START + this.id + Constant.PORT_END);
			} else if (line.startsWith(Constant.RES_LISTEN_PORT_HEADER)) {
				// 解析client返回的，他所侦听端口信息字符串
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
