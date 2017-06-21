package sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MACAddress {
	public MACAddress() {
	}

	public static String getMACAddress() {

		String address = "";
		String os = System.getProperty("os.name");
		System.out.println(os);
		if (os.startsWith("Windows")) {
			try {
				String command = "cmd.exe   /c   ipconfig   /all";
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p
				        .getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.indexOf("Physical") > 0 && line.indexOf("Address") > 0) {
						int index = line.indexOf(":");
						index += 2;
						address = line.substring(index);
						break;
					}
				}
				br.close();
				return address.trim();
			} catch (IOException e) {
			}
		} else if (os.startsWith("Mac") || os.startsWith("Linux") || os.startsWith("Unix")) {
			try {
				String command = "ifconfig";
				final String mark = "ether";
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p
				        .getInputStream()));
				String line, name = null;
				StringBuilder buf = new StringBuilder();
				while ((line = br.readLine()) != null) {
					if (!line.startsWith("\t") && !line.startsWith(" ")) {
						int iNameEnd = line.indexOf(":");
						if (iNameEnd != -1) {
							String tmp = line.substring(0, iNameEnd);
							if (name == null || !name.equals(tmp)) {
								name = tmp;
								buf.append(name).append(" ");
							}
						}
						continue;
					}
					int pos = line.indexOf(mark);
					if (pos > 0) {
						buf.append('(')
						        .append(line.substring(pos + mark.length()).trim())
						        .append("), ");
					}
				}
				br.close();
				if (buf.length() >= 2 && (buf.charAt(buf.length() - 2) == ','))
					buf.setLength(buf.length() - 2);
				address = buf.toString();
				return address.trim();
			} catch (IOException e) {
			}
		}
		return address;

	}

	public static void main(String[] args) {
		System.out.println("Physical Address:\n\t" + MACAddress.getMACAddress());
	}
}
