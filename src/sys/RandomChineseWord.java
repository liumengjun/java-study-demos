package sys;


import java.io.PrintWriter;

public class RandomChineseWord {
	public static void main(String[] args) {
		char ch;
		try{
			PrintWriter writer=new PrintWriter("ºº×Ö.txt");
			for(int i=19968;i<41870;i++){
				ch = (char)i;
				writer.print(i+":\t"+ch+",\t");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
