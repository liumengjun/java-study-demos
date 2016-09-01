package crypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 
 *
 */
public class PublicKeyTest {
	public static void main(String[] args) {
		System.out.println(CryptKeyPair.isPrime(65537));
		try {
			testPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <E,D> is a pair <public, private> key
	 * 
	 * @throws Exception
	 */
	public static void testPublicKey() throws Exception {
		CryptKeyPair keyPair = CryptKeyPair.generateKeyPair();
		System.out.println(keyPair);
		System.out.println();

		final String CHARSET_NAME = Charset.defaultCharset().name();// "MS932";
		System.out.println(CHARSET_NAME);
		// input message
		System.out.println("Please input one sentence:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in, CHARSET_NAME));
		String line = "";
		try {
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(line);

		// encode
		byte[] codes = line.getBytes(CHARSET_NAME);
		byte[] encBytes = encode(codes, keyPair);
		printArrayInHex(encBytes);
		// output
		// String encString = new String(encBytes);
		// System.out.println(encString);
		System.out.println();

		// decode
		byte[] decBytes = decode(encBytes, keyPair);
		printArrayInHex(decBytes);
		assert (Arrays.equals(codes, decBytes));
		// output
		String result = new String(decBytes, CHARSET_NAME);
		System.out.println(result);
		System.out.println(result.equals(line));
	}

	public static byte[] encode(byte[] bytes, CryptKeyPair keyPair) {
		if (bytes == null || keyPair == null) {
			throw new IllegalArgumentException();
		}
		final int COUNT = bytes.length;// CODE_MAX - CODE_MIN;
		int encode[] = new int[COUNT];
		// printArrayInHex(bytes);

		// encode
		BigInteger biN = BigInteger.valueOf(keyPair.N);
		BigInteger biE = BigInteger.valueOf(keyPair.E);
		for (int i = 0; i < COUNT; i++) {
			BigInteger X = BigInteger.valueOf(0xFF & bytes[i]);
			BigInteger biEnc = X.modPow(biE, biN);
			encode[i] = biEnc.intValue();
		}
		// printArrayInHex(encode);

		// package
		byte[] encBytes = new byte[COUNT * 3];
		for (int i = 0, offset = 0; i < COUNT; i++, offset += 3) {
			convInt3Byte(encode[i], encBytes, offset);
		}
		return encBytes;
	}

	public static byte[] decode(byte[] encBytes, CryptKeyPair keyPair) {
		if (encBytes == null || keyPair == null) {
			throw new IllegalArgumentException();
		}
		final int COUNT = encBytes.length / 3;
		byte[] decBytes = new byte[COUNT];

		// unpack
		int[] encInts = new int[COUNT];
		for (int i = 0, offset = 0; i < COUNT; i++, offset += 3) {
			encInts[i] = conv3Byte2Int(encBytes, offset);
		}

		// decode
		BigInteger decN = BigInteger.valueOf(keyPair.N);
		BigInteger decD = BigInteger.valueOf(keyPair.D);
		for (int i = 0; i < COUNT; i++) {
			BigInteger Y = BigInteger.valueOf(encInts[i]);
			BigInteger biDec = Y.modPow(decD, decN);
			decBytes[i] = biDec.byteValue();
		}
		return decBytes;
	}

	public static void printArrayInHex(int a[]) {
		if (a == null) {
			return;
		}
		StringBuilder sb = new StringBuilder(256);
		sb.append('[');
		for (int i = 0; i < a.length - 1; i++) {
			sb.append(getHex(a[i]) + ", ");
		}
		sb.append(getHex(a[a.length - 1]) + "]");
		System.out.println(sb);
	}

	public static void printArrayInHex(byte a[]) {
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

	private static void convInt3Byte(int a, byte[] bytes, int offset) {
		if (bytes == null || bytes.length < offset + 3) {
			throw new IllegalArgumentException();
		}
		bytes[offset] = (byte) a;
		bytes[++offset] = (byte) (a >> 8);
		bytes[++offset] = (byte) (a >> 16);
	}

	private static int conv3Byte2Int(byte[] bytes, int offset) {
		if (bytes == null || bytes.length < offset + 3) {
			throw new IllegalArgumentException();
		}
		int a = 0;
		a = 0xff & bytes[offset + 2];
		a = (a << 8) | 0xff & bytes[offset + 1];
		a = (a << 8) | 0xff & bytes[offset];
		return a;
	}

	private static class CryptKeyPair {
		// Example
		long P = 983; // P > CODE_MAX
		long Q = 991; // Q > CODE_MAX
		long N = P * Q;
		long M = (P - 1) * (Q - 1);
		long E = 997;
		// to make: E*D % M == 1
		long D = 953653;

		private CryptKeyPair() {
		}

		static CryptKeyPair generateKeyPair() {
			long p = PRIME_TABLE[(int) (Math.random() * PRIME_TABLE.length)];
			long q = PRIME_TABLE[(int) (Math.random() * PRIME_TABLE.length)];
			while (q == p) {
				q = PRIME_TABLE[(int) (Math.random() * PRIME_TABLE.length)];
			}
			long e = PRIME_TABLE[(int) (Math.random() * PRIME_TABLE.length)];
			while (e == p || e == q) {
				e = PRIME_TABLE[(int) (Math.random() * PRIME_TABLE.length)];
			}
			long n = p * q;
			long m = (p - 1) * (q - 1);
			long ee[] = extended_euclid(e, m);
			long d = ee[1]>0?ee[1]:m+ee[1];
			System.out.println("e=" + e + ",isPrime=" + isPrime(e));
			System.out.println("d=" + d + ",isPrime=" + isPrime(d));
			System.out.println("d=" + d + ",is pseudo_prime=" + pseudo_prime(d));
			System.out.println("d=" + d + ",is Probable Prime=" + BigInteger.valueOf(d).isProbablePrime(Integer.MAX_VALUE));
			CryptKeyPair keyPair = new CryptKeyPair();
			keyPair.P = p;
			keyPair.Q = q;
			keyPair.N = n;
			keyPair.M = m;
			keyPair.E = e;
			keyPair.D = d;
			return keyPair;
		}
		
		static long euclid(long a, long b){
			if (b==0){
				return a;
			} else {
				return euclid(b, a%b);
			}
		}
		
		static long[] extended_euclid(long a, long b) {
			if (b == 0) {
				return new long[]{a, 1, 0};// [d', x', y']
			} else {
				long[] ir = extended_euclid(b, a % b);
				long d = ir[0];// d = d'
				long x = ir[2];// x = y'
				long y = ir[1] - a/b * ir[2];// y = x' - a/b * y'
				return new long[]{d, x, y};
			}
		}

		static boolean isPrime(long d) {
			boolean flag = true;
			long r = (long) Math.sqrt(d);
			for (int i = 2; i <= r; i++) {
				if (d % i == 0) {
					flag = false;
					break;
				}
			}
			return flag;
		}
		
		static boolean pseudo_prime(long n) {
			BigInteger a = BigInteger.valueOf(2);
			BigInteger m = BigInteger.valueOf(n);
			BigInteger e = BigInteger.valueOf(n-1);
			return (a.modPow(e, m).equals(BigInteger.ONE));
		}

		public String toString() {
			return "P=" + P + ", Q=" + Q + ", N=" + N + ", M=" + M + ", E=" + E
					+ ", D=" + D;
		}

		private static final long PRIME_TABLE[] = { 257, 263, 269, 271, 277, 281,
				283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367,
				373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443,
				449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523,
				541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613,
				617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691,
				701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787,
				797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877,
				881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971,
				977, 983, 991, 997, };
	}

	private static final int CODE_MAX = 256; // P > CODE_MAX, Q > CODE_MAX
	private static final int CODE_MIN = 0;
}
