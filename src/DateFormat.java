import java.sql.Date;
import java.text.*;

public class DateFormat {
	public static void main(String[] args) {
		// currentTimeMillis 返回以毫秒为单位的时间
		long time = System.currentTimeMillis();
		Date d = new Date(time);
		System.out.println(d);// 结果 2006-06-27
		System.out.println(DateFormat.format(d));// 结果
		// 2006-06-27T19:24:35.450+0800
	}

	public static String format(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String s = sf.format(date);
		return s;
	}
}
