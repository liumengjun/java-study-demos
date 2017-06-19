package sys;

import java.io.PrintWriter;

/**
 * 输出汉字，文件编码GBK
 */
public class RandomChineseWord {
	public static void main(String[] args) {
		char ch;
		try {
			PrintWriter writer = new PrintWriter("text/汉字.txt");
			for (int i = 19968, j = 1; i < 40870; i++, j++) {
				ch = (char) i;
				writer.print(i + ":\t" + ch + ",\t");
				if (j == 16) {
					writer.print("\n");
					j = 0;
				}
			}
			writer.close();
			System.out.println("请看\"text/汉字.txt\"文件。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
