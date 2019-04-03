package deepseek;

import java.io.*;

public class CertificateOfIdentification {
	/*У����*/
	private final static byte[] checkCode ={'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
	/*��Ȩ����*/
	private final static int[] weightingFactor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	
	/**
	 * main test app
	 * @param args
	 * @throws IOException
	 */
	public final static void main(String[] args) throws IOException {
		
		/*console input*/
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("���������֤��:");
		String id;
		boolean flag = true;
		do {
			id = inputReader.readLine();
			if(id==null){
				System.out.println();
				System.exit(0);
			}
			id = id.trim();
			if(id.isEmpty()){
				System.out.println("ʲôҲû�У����������룺");
				continue;
			}
			byte[] bytes = id.getBytes();
			if(bytes.length!=15 && bytes.length!=18){
				System.out.println("��Ч�ĳ��ȣ����������룺");
				continue;
			}
			flag = false;
			for(int i=0;i<bytes.length;i++){
				if(!(bytes[i]=='X' || bytes[i]=='x') && (bytes[i]<'0' || bytes[i]>'9')){
					System.out.println("������ַ������������룺");
					flag = true;
					break;
				}
			}
		} while (flag);
		/*������������֤��*/
		byte[] correct = verifyID(id);
		if(correct==null){
			System.out.println("���֤��["+id+"]���Ϸ�!");
		}else{
			if(id.length()==15){
				System.out.println("����18λ���֤�ţ�"+new String(correct)+"��");
			}else{
				System.out.println("���֤��["+id+"]�Ϸ���");
			}
		}
	}

	/**
	 * ��֤��������֤����id
	 * @param id
	 * @return
	 */
	public static byte[] verifyID(String id) {
		byte[] bytes = id.getBytes();
		/*�����Ч�ĳ���*/
		int id_len = bytes.length;
		if(id_len!=15 && id_len!=18){
			return null;
		}

		/*��������ַ�*/
		boolean error = false;
		if(id_len==15){//����Ϊ15λʱ���ɰ汾������Ѳ���
			for(int i=0;i<id_len;i++){
				if(bytes[i]<'0' || bytes[i]>'9'){
					error = true;
					break;
				}
			}
			if(!error){
				byte[] temp = new byte[18];
				int i=0,j=0;
				for(;i<6;i++,j++) temp[j]=bytes[i];
				temp[j++]='1';temp[j++]='9';
				for(;i<15;i++,j++) temp[j]=bytes[i];
				bytes = null;
				bytes = temp;
			}
		}
		if(id_len==18){//����Ϊ18λ
			for(int i=0;i<id_len-1;i++){
				if(bytes[i]<'0' || bytes[i]>'9'){
					error = true;
					break;
				}
			}
			if(!(bytes[17]=='X' || bytes[17]=='x') && (bytes[17]<'0' || bytes[17]>'9')){
				error = true;
			}
		}
		if(error){
			return null;
		}
		
		/*��ȡ��������*/
		int[] ai = new int[17];
		for(int i=0;i<17;i++) ai[i] = bytes[i]-'0';
		
		/*System.out.println("a[1,2...17]:");
		for(int i=0;i<17;i++){
			System.out.print(ai[i]+" ");
		}
		System.out.println();
		System.out.println("W[1,2...17]:");
		for(int i=0;i<17;i++){
			System.out.print(weightingFactor[i]+" ");
		}
		System.out.println();*/	
		
		/*
		 * ��ǰ17λ���ֱ������Ȩ���
		 * ��ʽΪ��S = Sum(Ai * Wi), i = 0, ... , 16
		 */
		int sum = 0;
		for(int i=0;i<17;i++){
			sum += ai[i]*weightingFactor[i];
		}

		/*System.out.println("S=��(ai��Wi)");
		System.out.println("S="+sum);*/
		/*
		 * ��11�Լ�����ȡģ
		 * Y = mod(S, 11)
		 */
		int y = sum%11;
		/*System.out.println("Y=S(mod 11)");
		System.out.println("Y="+y);*/
		
		byte check_code = checkCode[y];
		/*System.out.println("У����=(12-Y)(mod 11){ע:10=X}");
		System.out.println("У����:"+(char)check_code);*/
		if(id_len==15){
			bytes[17] = check_code;
		}
		if(id_len==18){
			if(bytes[17]=='x'){
				bytes[17]='X';
			}
			if(check_code!=bytes[17]){
				return null;
			}
		}
		
		return bytes;
	}
}
