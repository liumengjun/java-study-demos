package sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IPNet {
	
	public static void main(String[] args) {
		System.out.println(getWinIp());
//		System.out.println(upWinIp());
	}

	public static Map<String, String> getWinIp() {
		Runtime runTime = Runtime.getRuntime();
		Map<String, String> map = null;
		try {
			// Process p = runTime.exec("net time");
			Process p = runTime.exec("ipconfig");
			BufferedReader pw = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = "";
			// 将doc命令查询出来的每一行信息存储在List集合中
			List<String> list = new ArrayList<String>();
			while ((str = pw.readLine()) != null) {
				if (str.length() > 0){
					list.add(str);
					System.out.println(str);
				}
			}
			// 如果一台机器上装了多个虚拟机，则会有多个网卡信息，所有要找出本地连接的信息
			int temp = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).indexOf("本地连接") > -1) {
					temp = i;
					break;
				}
			}
			// 会有两个IP地址，一个真实IP和一个物理IP，所有要加以区分，取得IP、子网掩码、网关
			map = new HashMap<String, String>();
			for (int i = 0; i < temp + 6; i++) {
				String[] ss = list.get(i).split(":");
				if (ss.length > 1) {
					if (ss[1].trim().length() > 6) {
						if (ss[0].indexOf("IP") > -1)
							map.put("Ip", ss[1].trim());
						else if (ss[0].indexOf("Mask") > -1)
							map.put("Mask", ss[1].trim());
						else if (ss[0].indexOf("Gateway") > -1)
							map.put("Gate", ss[1].trim());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

//	public static void upWinIp(LocalServer local) {
//		Runtime runTime = Runtime.getRuntime();
//		String doc = "netsh interface ip set address \"本地连接\" static " + local.getIp() + " " + local.getMask() + " "
//				+ local.getGate() + " 1";
//		try {
//			Process p = runTime.exec(doc);
//			BufferedReader pw = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			String str = "";
//			while ((str = pw.readLine()) != null) {
//				System.out.println(str);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
