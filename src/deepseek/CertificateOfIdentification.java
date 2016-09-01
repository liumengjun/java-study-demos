package deepseek;

import java.io.*;

public class CertificateOfIdentification {
	/*校验码*/
	private final static byte[] checkCode ={'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
	/*加权因子*/
	private final static int[] weightingFactor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	
	/**
	 * main test app
	 * @param args
	 * @throws IOException
	 */
	public final static void main(String[] args) throws IOException {
		
		/*console input*/
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入身份证号:");
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
				System.out.println("什么也没有，请重新输入：");
				continue;
			}
			byte[] bytes = id.getBytes();
			if(bytes.length!=15 && bytes.length!=18){
				System.out.println("无效的长度，请重新输入：");
				continue;
			}
			flag = false;
			for(int i=0;i<bytes.length;i++){
				if(!(bytes[i]=='X' || bytes[i]=='x') && (bytes[i]<'0' || bytes[i]>'9')){
					System.out.println("错误的字符，请重新输入：");
					flag = true;
					break;
				}
			}
		} while (flag);
		/*检验输入的身份证号*/
		byte[] correct = verifyID(id);
		if(correct==null){
			System.out.println("身份证号["+id+"]不合法!");
		}else{
			if(id.length()==15){
				System.out.println("现行18位身份证号："+new String(correct)+"。");
			}else{
				System.out.println("身份证号["+id+"]合法。");
			}
		}
	}

	/**
	 * 验证输入的身份证号码id
	 * @param id
	 * @return
	 */
	public static byte[] verifyID(String id) {
		byte[] bytes = id.getBytes();
		/*检查无效的长度*/
		int id_len = bytes.length;
		if(id_len!=15 && id_len!=18){
			return null;
		}

		/*检查错误的字符*/
		boolean error = false;
		if(id_len==15){//长度为15位时，旧版本，大多已不用
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
		if(id_len==18){//长度为18位
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
		
		/*提取号码数字*/
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
		 * 对前17位数字本体码加权求和
		 * 公式为：S = Sum(Ai * Wi), i = 0, ... , 16
		 */
		int sum = 0;
		for(int i=0;i<17;i++){
			sum += ai[i]*weightingFactor[i];
		}

		/*System.out.println("S=∑(ai×Wi)");
		System.out.println("S="+sum);*/
		/*
		 * 以11对计算结果取模
		 * Y = mod(S, 11)
		 */
		int y = sum%11;
		/*System.out.println("Y=S(mod 11)");
		System.out.println("Y="+y);*/
		
		byte check_code = checkCode[y];
		/*System.out.println("校验码=(12-Y)(mod 11){注:10=X}");
		System.out.println("校验码:"+(char)check_code);*/
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
