package regex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexText {
	static void test(){
		Pattern p = null; //������ʽ      
		Matcher m = null; //�������ַ���  
		boolean b = false;
		/*
		//������ʽ��ʾ����ĸ��a���м��������ַ�����β��b����
		//��ƥ��Ľ��
		p = Pattern.compile("a*b");    
		m = p.matcher("baaaaab");    
		b = m.matches();    
		System.out.println("ƥ������"+b); //�����false 
		  
		//ƥ��Ľ��
		p = Pattern.compile("a*b");    
		m = p.matcher("aaaaab");    
		b = m.matches();    
		System.out.println("ƥ������"+b); //�����true 
		*/
		
		p = Pattern.compile("a[a-z]*b");	//������ʽ    
		m = p.matcher("aabaavabb");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);
	}
	
	public static void main(String argus[]){
		test();
		
		String reference = "h*";
		String current = "hello";
		
		System.out.println(reference);
		System.out.println(current);
		Pattern p = null; //������ʽ      
		Matcher m = null; //�������ַ���  
		boolean b = false;
		p = Pattern.compile("h(.)*");	//������ʽ    
		m = p.matcher("hel\tlo");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);
		
		p = Pattern.compile("h(.)*");	//������ʽ    
		m = p.matcher("hel\n\tlo");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);
		
		p = Pattern.compile(".");	//������ʽ    
		m = p.matcher("we");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);
		
		p = Pattern.compile(".");	//������ʽ    
		m = p.matcher("w");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);

		p = Pattern.compile("\\.");	//������ʽ    
		m = p.matcher(".");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);

		p = Pattern.compile("\\.");	//������ʽ    
		m = p.matcher("w");			//�������ַ���
		b = m.matches();    
		System.out.println("ƥ������"+b);
	}
}
