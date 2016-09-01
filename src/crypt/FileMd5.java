package crypt;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class FileMd5 {
	public static void main(String[] args) throws Exception {

		if (args.length < 1 || args[0].isEmpty()) {
			System.err
					.println("Please input the filename to calculate its MD5 value!");
			return;
		}
		String fileName = args[0];
		File file = new File(fileName);
		System.out.println("File: " + file.getName());
		System.out.println("Path: " + file.getAbsolutePath());
		System.out.println("Size: " + file.length() + "B");

		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
				file.length());

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(byteBuffer);

		byte[] md5Bytes = md5.digest();
		System.out.println(" MD5: " + bufferToHex(md5Bytes));
	}

	private static String bufferToHex(byte bytes[]) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuffer stringbuffer = new StringBuffer(2 * bytes.length);
		for (int l = 0; l < bytes.length; l++) {
			byte bt = bytes[l];
			char c0 = hexDigits[(bt & 0xf0) >> 4];
			char c1 = hexDigits[bt & 0xf];
			stringbuffer.append(c0);
			stringbuffer.append(c1);
		}
		return stringbuffer.toString();
	}

}
