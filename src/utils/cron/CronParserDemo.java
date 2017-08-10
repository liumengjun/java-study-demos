package utils.cron;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.cronutils.model.CronType.QUARTZ;

/**
 * Created by liumengjun on 2017-08-10.
 */
public class CronParserDemo {
    public static void main(String[] args) {
        //get a predefined instance
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);

        //create a descriptor for a specific Locale
        CronDescriptor descriptor = CronDescriptor.instance(Locale.CHINA);

        //create a parser based on provided definition
        CronParser parser = new CronParser(cronDefinition);
        Cron quartzCron = parser.parse("* * * 1-3 * ? *");
        System.out.println(quartzCron.asString());
        System.out.println(descriptor.describe(quartzCron));
        //validate expression
        quartzCron.validate();

        //parse some expression and ask descriptor for description
        Cron cron2 = parser.parse("*/45 * * * * ?");
        System.out.println(cron2.asString());
        //description will be: "every 45 seconds"
        System.out.println(descriptor.describe(cron2));


        System.out.println("----------------------------------------");
        // TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Date now = new Date();
        System.out.println(now);
        System.out.println(now.toGMTString());

        /*
        Both, a day-of-week AND a day-of-month parameter, are not supported.
        不支持同时设置day-of-month 和 day-of-week，所以必须有一个为问号`?`
        NOTE: 表达不了9:20-5:30
         */
        // "* * * */5 * ?" == 1/5: 每月第一天，然后每隔五天
        // "* * * 1,5/5 * ?" == 1,5,10,15,20,25,30: 每月第一天，和每月日子个位是5或0
        Cron testCron1 = parser.parse("* */3 * * * ?");
        System.out.println(descriptor.describe(testCron1));
        System.out.println(isMatch(testCron1, now));
        System.out.println(isMatch(testCron1, now, tz));

        Cron testCron2 = parser.parse("* * * 1,5/5 * ?");
        System.out.println(descriptor.describe(testCron2));
        System.out.println(isMatch(testCron2, now));
        System.out.println(isMatch(testCron2, now, tz));

        Cron testCron3 = parser.parse("* * 9-17 ? * MON-FRI");
        System.out.println(descriptor.describe(testCron3));
        System.out.println(isMatch(testCron3, now));
        System.out.println(isMatch(testCron3, now, tz));

        // 9:20am-5:30pm 无法用一条cron expr表示
        Cron[] timeToTime = new Cron[]{
            parser.parse("* 20-59 9 ? * MON-FRI"),
            parser.parse("* * 10-17 ? * MON-FRI"),
            parser.parse("* 0-29 17 ? * MON-FRI")
        };
        System.out.println("9:20am-5:30pm");
        System.out.println(isMatch(timeToTime, now));
        System.out.println(isMatch(timeToTime, now, tz));
    }

    private static boolean isMatch(Cron cron, Date date) {
        return isMatch(cron, date, null);
    }

    private static boolean isMatch(Cron cron, Date date, TimeZone timeZone) {
        ExecutionTime execTime = ExecutionTime.forCron(cron);
        Instant instant = Instant.ofEpochMilli(date.getTime());
        ZoneId zone = timeZone == null ? ZoneId.systemDefault() : ZoneId.of(timeZone.getID());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zone);
        return execTime.isMatch(zonedDateTime);
    }

    private static boolean isMatch(Cron[] crons, Date date) {
        return isMatch(crons, date, null);
    }

    private static boolean isMatch(Cron[] crons, Date date, TimeZone timeZone) {
        for (Cron cron : crons) {
            if (isMatch(cron, date, timeZone)) {
                return true;
            }
        }
        return false;
    }
}
