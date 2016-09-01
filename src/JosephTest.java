public class JosephTest {

	private static long startTime;
	private static long endTime;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		start();
		joseph(2, 50);
		end();
	}

	/**
	 * 共n个，每d个周期，最后剩下d-1个
	 * 
	 * @param d
	 * @param n
	 */
	public static void joseph(final int d, final int n) {
		if (d < 2) {
			return;
		}
		final int TOKEN_OK = 0;
		final int TOKEN = 1;
		final int[] a = new int[n];

		int count = 0; // the number of being marked
		int round = 0; // the number of circle count
		int pos = 0;
		while (count <= (n - d)) {
			int time = 0;
			while (true) {
				if (a[pos] == TOKEN_OK) {
					if (time < d - 1) {
						time++;
					} else {
						break;
					}
				}
				if (pos + 1 >= n) {
					round++;
					pos = pos + 1 - n;
					{
						for (int i = 0; i < n; i++) {
							System.out.print(a[i] + ",");
						}
						System.out.println();
					}
				} else {
					pos++;
				}
			}
			a[pos] = TOKEN + round;
			count++;
			if (pos + 1 >= n) {
				round++;
				pos = pos + 1 - n;
				{
					for (int i = 0; i < n; i++) {
						System.out.print(a[i] + ",");
					}
					System.out.println();
				}
			} else {
				pos++;
			}
		}

		for (int i = 0; i < n; i++) {
			System.out.print(a[i] + ",");
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			if (a[i] == TOKEN_OK) {
				System.out.print("[" + (i + 1) + "],");
			}
		}
		System.out.println();
		System.out.println("Round Time's:" + round);
		System.out.println();
	}

	private static void start() {
		startTime = System.currentTimeMillis();
	}

	private static void end() {
		endTime = System.currentTimeMillis();
		System.out.println("耗时:" + (endTime - startTime) + "毫秒");
	}
}
