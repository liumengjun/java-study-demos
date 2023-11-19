package _test2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by liumengjun on 2023-11-19.
 */
public class WeirdTime {
    public static void main(String[] args) throws Exception {
        Calendar me = Calendar.getInstance();
        me.set(1989, 4 - 1, 16, 0, 0, 0);
//        me.set(2000, 4 - 1, 16, 0, 0, 0);
        me.set(Calendar.MILLISECOND, 0);
        Date d = me.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String h = sdf.format(d);
        System.out.println(h);
        System.out.println(d);
        System.out.println(me.get(Calendar.YEAR) + "-"
                + (me.get(Calendar.MONTH) + 1) + "-"
                + me.get(Calendar.DAY_OF_MONTH) + " "
                + me.get(Calendar.HOUR_OF_DAY) + ":" + me.get(Calendar.MINUTE)
                + ":" + me.get(Calendar.SECOND));
        // ==
        long ts = d.getTime();
        System.out.println(ts);
        System.out.println(sdf.format(new Date(ts)));
        long weirdCount = 0, totalDays = 30 * 365;
        final String OOTimeStr = "00:00:00";
        for (long i = 0; i <= totalDays; i++) {
            // 这里`i`不能使用`int`类型
            long lts1 = ts - i * 1000 * 60 * 60 * 24;
            Date od1 = new Date(lts1);
            String s1 = sdf.format(od1);
            if (!s1.endsWith(OOTimeStr)) {
                weirdCount++;
                System.out.printf("-%4d: %s, ts: %d%n", i, s1, lts1);
//                break;
            }
            long lts2 = ts + i * 1000 * 60 * 60 * 24;
            Date od2 = new Date(lts2);
            String s2 = sdf.format(od2);
            if (!s2.endsWith(OOTimeStr)) {
                weirdCount++;
                System.out.printf("+%4d: %s, ts: %d%n", i, s2, lts2);
//                break;
            }
        }
        System.out.println("weirdCount: " + weirdCount + ", rate: " + weirdCount * 100 / 2 / totalDays + "%");
    }

}
