package algorism.math;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordStringGen {
	
	/*MD5 生成器*/
	private static MessageDigest md = null;
	static{
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("程序出错，请重新安装jdk");
			System.exit(-1);
		}
	}
	/*十六进制字符集*/
	private static final char[] hex_chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	//字符集
	private static char[] charSet = {' '};
	//排列组合计数
	private static int numOfCombo = 0;

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			args = new String[] {"-d", "-i", "-add", "-_", "3"};
		PasswordStringGen.process(args);
	}

	/**
	 * 程序开始main方法
	 * @param args
	 * @throws Exception
	 */
	public static void process(String args[]) throws Exception {
		int num = -1;
		boolean digitals = false;
		boolean insensitive = false;
		boolean onlyInt = false;
		boolean onlyUpper = false;
		boolean space = false;
		String addSpecialCharsStr = "";
		if(args.length>0 && args.length<8){
			for(int i=0;i<args.length;i++){
				String temp = args[i];
				if(temp.equals("-h") || temp.equals("--help")){
					help();
				}
				if(temp.equals("-d")){
					digitals = true;
					continue;
				}
				if(temp.equals("-i")){
					insensitive = true;
					continue;
				}
				if(temp.equals("-oi")){
					onlyInt = true;
					continue;
				}
				if(temp.equals("-ou")){
					onlyUpper = true;
					continue;
				}
				if(temp.equals("-sp")){
					space = true;
					continue;
				}
				if(temp.equals("-add")){
					i++;
					if(i>=args.length){
						help();
					}
					addSpecialCharsStr = args[i];
					continue;
				}
				try{
					if(num==-1){
						num = Integer.parseInt(temp);
					}
				}catch(Exception ex){}
			}
		}
		if( num==-1 || (onlyInt && onlyUpper) ){
			help();
		}
		initCharSet(digitals,insensitive,onlyInt,onlyUpper,space,addSpecialCharsStr);
		genDictString(num);
	}
	
	/**
	 * 更具各个选项初始化字符集，具体参数意义参看help()方法
	 * @param digitals
	 * @param insensitive
	 * @param onlyInt
	 * @param onlyUpper
	 * @param space
	 * @param addSpecialCharsStr
	 */
	private static void initCharSet(boolean digitals, boolean insensitive,
			boolean onlyInt, boolean onlyUpper,
			boolean space, String addSpecialCharsStr){
		if(addSpecialCharsStr==null){
			addSpecialCharsStr = "";
		}
		int size = 26;
		int n = 0;
		if(onlyInt){
			size = 10;
			if(space) size += 1;
			for(int i=0;i<addSpecialCharsStr.length();i++){
				char c_temp = addSpecialCharsStr.charAt(i);
				if(c_temp!=' ' && !(c_temp>='0' && c_temp<='9') && !(c_temp>='A' && c_temp<='Z') && !(c_temp>='a' && c_temp<='z') ){
					size++;
				}
			}
			charSet = new char[size];
			n = 0;
			for(int i=0; i<10; i++){
				charSet[n++] = (char)('0' + i);
			}
			if(space){
				charSet[n++] = ' ';
			}
			for(int i=0;i<addSpecialCharsStr.length();i++){
				char c_temp = addSpecialCharsStr.charAt(i);
				if(c_temp!=' ' && !(c_temp>='0' && c_temp<='9') && !(c_temp>='A' && c_temp<='Z') && !(c_temp>='a' && c_temp<='z') ){
					charSet[n++] = c_temp;
				}
			}
			return;
		}else if(onlyUpper){
			size = 26;
			if(space) size += 1;
			for(int i=0;i<addSpecialCharsStr.length();i++){
				char c_temp = addSpecialCharsStr.charAt(i);
				if(c_temp!=' ' && !(c_temp>='0' && c_temp<='9') && !(c_temp>='A' && c_temp<='Z') && !(c_temp>='a' && c_temp<='z') ){
					size++;
				}
			}
			charSet = new char[size];
			n = 0;
			for(int i=0; i<26; i++){
				charSet[n++] = (char)('A' + i);
			}
			if(space){
				charSet[n++] = ' ';
			}
			for(int i=0;i<addSpecialCharsStr.length();i++){
				char c_temp = addSpecialCharsStr.charAt(i);
				if(c_temp!=' ' && !(c_temp>='0' && c_temp<='9') && !(c_temp>='A' && c_temp<='Z') && !(c_temp>='a' && c_temp<='z') ){
					charSet[n++] = c_temp;
				}
			}
			return;
		}else{
			size = 26;
			if(digitals) size += 10;
			if(insensitive) size += 26;
			if(space) size += 1;
			for(int i=0;i<addSpecialCharsStr.length();i++){
				char c_temp = addSpecialCharsStr.charAt(i);
				if(c_temp!=' ' && !(c_temp>='0' && c_temp<='9') && !(c_temp>='A' && c_temp<='Z') && !(c_temp>='a' && c_temp<='z') ){
					size++;
				}
			}
			charSet = new char[size];
			n = 0;
			char initialChar = 'a';
			for(int i=0; i<26; i++){
				charSet[n++] = (char)(initialChar + i);
			}
			if(digitals){
				initialChar = '0';
				for(int i=0; i<10; i++){
					charSet[n++] = (char)(initialChar + i);
				}
			}
			if(insensitive){
				initialChar = 'A';
				for(int i=0; i<26; i++){
					charSet[n++] = (char)(initialChar + i);
				}
			}
			if(space){
				charSet[n++] = ' ';
			}
			for(int i=0;i<addSpecialCharsStr.length();i++){
				char c_temp = addSpecialCharsStr.charAt(i);
				if(c_temp!=' ' && !(c_temp>='0' && c_temp<='9') && !(c_temp>='A' && c_temp<='Z') && !(c_temp>='a' && c_temp<='z') ){
					charSet[n++] = c_temp;
				}
			}
		}
	}
	
	/**
	 * 显示程序应用帮助，然后退出程序
	 */
	private static void help(){
		System.out.println("please use: prog [-d] [-i] [-oi] [-ou] [-sp] [-add specialCharsStr] num \n"+
					"    default charset is all lower-case letters \n"+
					"    -d : include integer digitals \n"+
					"    -i : case-insensitive \n"+
					"    -oi: no lower-case letters, only integer digitals \n"+
					"    -ou: no lower-case letters, only upper-case letters \n"+
					"    -sp: include the space character \n"+
					"    -add specialCharsStr: \n" +
					"         include additional special characters \n" +
					"         e.g. \"@#(!\" \n"
					);
		System.exit(0);
	}
	
	/**
	 * 生成字典字符串
	 */
	public static void genDictString(int num){
		if(num<1 || num>256){//限定字符个数
			return;
		}
		//字符集大小
		int setSize = charSet.length;
		
		//计算应有排列组合数
		int numExpected = 1;
		for(int i=0;i<num;i++){
			numExpected *= setSize;
		}
		System.out.println("Expecting number:"+setSize+"^"+num+"="+numExpected);
		
		char[] stringChars = new char[num];//store gened string chars

		numOfCombo = 0;
		//递归生成字符串全排列
		generateNCharsComboString(num, 1, stringChars);
		System.out.println("numOfCombo:"+numOfCombo);
	}
	
	/**
	 * 由charSet中字符，生成numOfChars个字符全排列
	 * @param numOfChars
	 * @param curCharLoc :从1到numOfChars
	 * @param stringChars
	 */
	private static void generateNCharsComboString(int numOfChars, int curCharLoc, char[] stringChars) {
		//从当前位置开始，排序
		int setSize = charSet.length;
		int iLoc=curCharLoc-1;
		
		//当前位置iLoc，遍历所有的charSet字符
		for(int iCharPos=0; iCharPos<setSize; iCharPos++){
			stringChars[iLoc] = charSet[iCharPos];
			//iLoc是最后一位才输出
			if(iLoc==numOfChars-1){
				numOfCombo++;
				String geneStr = new String(stringChars);
				//System.out.println(geneStr);
				String md5Str = genMD5String(geneStr);
				System.out.println(geneStr+"\t: "+md5Str);
				continue;
			}
			//递归：将重点移到下一位，进行排列
			generateNCharsComboString(numOfChars, curCharLoc+1, stringChars);
		}
	}
	
	/**
	 * 生成给定字符串srcStr的MD5值
	 * return md5(srcStr);
	 */
	public static String genMD5String(String srcStr){
		try{
			md.update(srcStr.getBytes("UTF8"));
			byte[] mdBytes = md.digest();
			StringBuffer resultBuf = new StringBuffer(32);
			for (int i = 0; i < mdBytes.length; i++) {
				byte b_temp = mdBytes[i];
				resultBuf.append(hex_chars[b_temp >>> 4 & 0xf]);
				resultBuf.append(hex_chars[b_temp & 0xf]);
			}
			return resultBuf.toString();
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return null;
	}
}
