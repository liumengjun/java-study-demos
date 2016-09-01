package basic;
import java.io.*;
import java.security.*;

/**
 * 字符的十六进制转换
 * 关于byte:    signed byte 把 0x00 ~ 0xff
 * 映射成范围 0~127和 -128~-1    两段,比较简单的办法用
 * (b+256)%256的办法令其值回到0~255，或者用&0xff并赋给一个int。
 * 参考http://www.jsfsoft.com:8080/beyond-pebble/pinxue/2006/08/23/1156309692525.html
 * 
 * @author zhonglijunyi
 * @version 0
 */
public class temp {
	
	public static void main(String[] args) {
		char ch = (char)-1;
		byte by = (byte)-2;
		int char_1 = 0xffff & ch;
		int byte_2 = 0xff & by;
		System.out.println("(char)-1:" + char_1 + "\tchar:" + ch +
				"\nHex:" + Integer.toHexString(char_1));
		System.out.println("(byte)-2:" + byte_2 + "\tbyte:" + by +
				"\nHex:" + Integer.toHexString(byte_2));

		for(int i = 0; i < 0xff; i++){
			int temp = i << 8;
			int int_ch = 0xff | temp;
			char chch = (char)int_ch;
			System.out.println("int_ch in Hex:" + Integer.toHexString(int_ch)
					+ "\tchar:" + chch);
			
		}
		
		System.out.println("MD5Encode(\"H\"):" + bytesToHexString(MD5Encode("H")));
		System.out.print("str2Bcd(\"123\"):");
		byte[] sf = str2Bcd("123");
		for(int i = 0; i < sf.length; i++){
			System.out.print(sf[i] + "\t");
		}
		System.out.println();
		
		byte[] bcd = {9,1,25,1};
		System.out.println(bcd2Str(bcd));
	}
	
	public static byte[] hexStringToByte(String hex) {
	    int len = (hex.length() / 2);
	    byte[] result = new byte[len];
	    char[] achar = hex.toCharArray();
	    for (int i = 0; i < len; i++) {
	     int pos = i * 2;
	     result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
	    }
	    return result;
	}

	private static byte toByte(char c) {
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);
	    return b;
	}

	/** *//**
	    * 把字节数组转换成16进制字符串
	    * @param bArray
	    * @return
	    */
	public static final String bytesToHexString(byte[] bArray) {
	    StringBuffer sb = new StringBuffer(bArray.length);
	    String sTemp;
	    for (int i = 0; i < bArray.length; i++) {
	     sTemp = Integer.toHexString(0xFF & bArray[i]);
	     if (sTemp.length() < 2)
	      sb.append(0);
	     sb.append(sTemp.toUpperCase());
	    }
	    return sb.toString();
	}

	/** *//**
	    * 把字节数组转换为对象
	    * @param bytes
	    * @return
	    * @throws IOException
	    * @throws ClassNotFoundException
	    */
	public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
	    ObjectInputStream oi = new ObjectInputStream(in);
	    Object o = oi.readObject();
	    oi.close();
	    return o;
	}

	/** *//**
	    * 把可序列化对象转换成字节数组
	    * @param s
	    * @return
	    * @throws IOException
	    */
	public static final byte[] objectToBytes(Serializable s) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream ot = new ObjectOutputStream(out);
	    ot.writeObject(s);
	    ot.flush();
	    ot.close();
	    return out.toByteArray();
	}

	public static final String objectToHexString(Serializable s) throws IOException{
	    return bytesToHexString(objectToBytes(s));
	}

	public static final Object hexStringToObject(String hex) throws IOException, ClassNotFoundException{
	    return bytesToObject(hexStringToByte(hex));
	}

	/** *//**
	    * @函数功能: BCD码转为10进制串(阿拉伯数据)
	    * @输入参数: BCD码
	    * @输出结果: 10进制串
	    */
	public static String bcd2Str(byte[] bytes){
	    StringBuffer temp=new StringBuffer(bytes.length*2);

	    for(int i=0;i<bytes.length;i++){
	     temp.append((byte)((bytes[i]& 0xf0)>>>4));
	     temp.append((byte)(bytes[i]& 0x0f));
	    }
	    //return temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString();
	    return temp.toString();
	}

	/** *//**
	    * @函数功能: 10进制串转为BCD码
	    * @输入参数: 10进制串
	    * @输出结果: BCD码
	    */
	public static byte[] str2Bcd(String asc) {
	    int len = asc.length();
	    int mod = len % 2;

	    if (mod != 0) {
	     asc = "0" + asc;
	     len = asc.length();
	    }

	    byte abt[] = new byte[len];
	    if (len >= 2) {
	     len = len / 2;
	    }

	    byte bbt[] = new byte[len];
	    abt = asc.getBytes();
	    int j, k;

	    for (int p = 0; p < asc.length()/2; p++) {
	     if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
	      j = abt[2 * p] - '0';
	     } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
	      j = abt[2 * p] - 'a' + 0x0a;
	     } else {
	      j = abt[2 * p] - 'A' + 0x0a;
	     }

	     if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
	      k = abt[2 * p + 1] - '0';
	     } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
	      k = abt[2 * p + 1] - 'a' + 0x0a;
	     }else {
	      k = abt[2 * p + 1] - 'A' + 0x0a;
	     }

	     int a = (j << 4) + k;
	     byte b = (byte) a;
	     bbt[p] = b;
	    }
	    return bbt;
	}
	/** *//**
	    * @函数功能: BCD码转ASC码
	    * @输入参数: BCD串
	    * @输出结果: ASC码
	    */
	/*public static String BCD2ASC(byte[] bytes) {
	    StringBuffer temp = new StringBuffer(bytes.length * 2);

	    for (int i = 0; i < bytes.length; i++) {
	     int h = ((bytes[i] & 0xf0) >>> 4);
	     int l = (bytes[i] & 0x0f);   
	     temp.append(BToA[h]).append( BToA[l]);
	    }
	    return temp.toString() ;
	}*/

	/** *//**
	    * MD5加密字符串，返回加密后的16进制字符串
	    * @param origin
	    * @return
	    */
	public static String MD5EncodeToHex(String origin) { 
	       return bytesToHexString(MD5Encode(origin));
	     }

	/** *//**
	    * MD5加密字符串，返回加密后的字节数组
	    * @param origin
	    * @return
	    */
	public static byte[] MD5Encode(String origin){
	    return MD5Encode(origin.getBytes());
	}

	/** *//**
	    * MD5加密字节数组，返回加密后的字节数组
	    * @param bytes
	    * @return
	    */
	public static byte[] MD5Encode(byte[] bytes){
	    MessageDigest md=null;
	    try {
	     md = MessageDigest.getInstance("MD5");
	     return md.digest(bytes);
	    } catch (NoSuchAlgorithmException e) {
	     e.printStackTrace();
	     return new byte[0];
	    }
	  
	}
}
