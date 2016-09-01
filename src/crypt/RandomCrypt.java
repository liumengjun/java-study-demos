package crypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class RandomCrypt {
	private static final Random cipherGen = new Random();
	private static long cipher = 0x0137F7310137F731L;
	private static final int codeMax = 256;

	public static void main(String[] args) {
		// input message
		System.out.println("Please input one sentence:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		try {
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(line);

		init();
		// encode
		byte[] codes = line.getBytes();
		byte[] encBytes = encode(codes);
		System.out.println(new String(encBytes));
		printArrayInHex(encBytes);
		// output
		// String encString = new String(encBytes);
		// System.out.println(encString);
		System.out.println();

		// decode
		byte[] decBytes = decode(encBytes);
		printArrayInHex(decBytes);
		assert (Arrays.equals(codes, decBytes));
		// output
		String result = new String(decBytes);
		System.out.println(result);
		System.out.println(result.equals(line));
	}
	
	public static void init() {
		cipher = cipherGen.nextLong();
	}

	public static byte[] encode(byte[] plaintext) {
		byte[] ciphertext = new byte[plaintext.length];
		cipherGen.setSeed(cipher);
		for (int i=0; i<plaintext.length; i++) {
			ciphertext[i] = (byte)(plaintext[i] ^ cipherGen.nextInt(codeMax));
		}
		return ciphertext;
	}

	public static byte[] decode(byte[] ciphertext) {
		byte[] plaintext = new byte[ciphertext.length];
		cipherGen.setSeed(cipher);
		for (int i=0; i<ciphertext.length; i++) {
			plaintext[i] = (byte)(ciphertext[i] ^ cipherGen.nextInt(codeMax));
		}
		return plaintext;
	}

	private static void printArrayInHex(byte a[]) {
		if (a == null) {
			return;
		}
		StringBuilder sb = new StringBuilder(256);
		sb.append('[');
		for (int i = 0; i < a.length - 1; i++) {
			sb.append(getHex(0xFF & a[i]) + ", ");
		}
		sb.append(getHex(0xFF & a[a.length - 1]) + "]");
		System.out.println(sb);
	}

	private static String getHex(int a) {
		String s = Integer.toHexString(a);
		s = s.toUpperCase();
		if (s.length() % 2 == 1) {
			s = "0x0" + s;
		} else {
			s = "0x" + s;
		}
		return s;
	}
}
