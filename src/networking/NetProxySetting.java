package networking;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

public abstract class NetProxySetting {

	public static void initSetting(String host, int port, String userName,
			String password) {
		Properties prop = System.getProperties();
		// 设置http访问要使用的代理服务器的地址
		prop.setProperty("http.proxyHost", host);
		// 设置http访问要使用的代理服务器的端口
		prop.setProperty("http.proxyPort", "" + port);
		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		// prop.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");

		// 设置安全访问使用的代理服务器地址与端口
		// 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
		prop.setProperty("https.proxyHost", host);
		prop.setProperty("https.proxyPort", "443");

		// 使用ftp代理服务器的主机、端口以及不需要使用ftp代理服务器的主机
		prop.setProperty("ftp.proxyHost", host);
		prop.setProperty("ftp.proxyPort", "2121");
		// prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");

		// socks代理服务器的地址与端口
		prop.setProperty("socksProxyHost", host);
		prop.setProperty("socksProxyPort", "" + port);

		// 设置登陆到代理服务器的用户名和密码
		Authenticator.setDefault(new MyAuthenticator(userName, password));

	}

	private static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}
}
