package sys;

import java.io.PrintWriter;

/**
 * ������֣��ļ�����GBK
 */
public class RandomChineseWord {
	public static void main(String[] args) {
		char ch;
		try {
			PrintWriter writer = new PrintWriter("text/����.txt");
			for (int i = 19968, j = 1; i < 40870; i++, j++) {
				ch = (char) i;
				writer.print(i + ":\t" + ch + ",\t");
				if (j == 16) {
					writer.print("\n");
					j = 0;
				}
			}
			writer.close();
			System.out.println("�뿴\"text/����.txt\"�ļ���");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
