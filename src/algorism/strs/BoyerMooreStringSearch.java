package algorism.strs;

import java.util.Random;

public class BoyerMooreStringSearch {

	/**
	 * Boyer-Moore String Search Algorithm
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static int bmIndex(String src, String dst) {
		int sl = src.length(), dl = dst.length(), imax = dl - 1, dp = imax, sp = imax;
		int span = imax - dst.lastIndexOf(dst.charAt(dp), dl - 2);
		while (sp < sl) {
			while (dst.charAt(dp) == src.charAt(sp)) {
				if (dp == 0) {
					return sp;
				}
				dp--;
				sp--;
			}
			int num = imax - dp; // suffix length
			int move = dp - dst.lastIndexOf(src.charAt(sp));
			if (num > 0 && span > move) {
				move = span; // move = max{span, move}
			}
			sp += num + move;
			dp = imax;
		}
		return -1;
	}

	public static void main(String[] args) {
		String o = "HERE IS A SIMPLE EXAMPLE";
		String s = "EXAMPLE";
		System.out.println(o.indexOf(s));
		System.out.println(bmIndex(o, s));

		String randomStr = randomStr(4 * 1024 * 1024);
		System.out.println("Testing Boyer-Moore string search... ");
		// System.out.println(randomStr);
		int miss = 0, ok = 0, TOTAL = 10000;
		for (int i = 0; i < TOTAL; i++) {
			String tmp = randomStr(1 + new Random().nextInt(8));
			int expect = randomStr.indexOf(tmp);
			int real = bmIndex(randomStr, tmp);
			if (expect == -1) {
				miss++;
			}
			if (expect != real) {
				System.out.println("Wrong search '" + tmp + "'.");
			} else {
				ok++;
			}
		}
		System.out.println("right:" + ok + ", wrong:" + (TOTAL - ok)
		        + ", got: " + (TOTAL - miss) + ", miss:" + miss);
		System.out.println("End testing.");
	}

	private static String randomStr(int len) {
		char[] buf = new char[len];
		Random rand = new Random();
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (char) ('A' + rand.nextInt('z' - 'A'));
		}
		return new String(buf);
	}
}
