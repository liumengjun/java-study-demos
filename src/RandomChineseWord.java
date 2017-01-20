

import java.io.PrintWriter;

public class RandomChineseWord {
	public static void main(String[] args) {
		char ch;
		try{
			PrintWriter writer=new PrintWriter("text/ºº×Ö.txt");
			for(int i=19968,j=1;i<40869;i++,j++){
				ch = (char)i;
				writer.print(i+":\t"+ch+",\t");
				if(j==16){
					writer.print("\n");
					j=0;
				}
			}
			writer.close();
			System.out.println("Çë¿´\"text/ºº×Ö.txt\"ÎÄ¼þ¡£");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
