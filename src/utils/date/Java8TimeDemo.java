package utils.date;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.lang.System.out;

/**
 * Created by liumengjun on 2017-08-24.
 */
public class Java8TimeDemo {

    public static void main(String[] args) {
        demoDiff();
    }

    static void demoDiff() {
        // 现在
        LocalDateTime now = LocalDateTime.now();
        out.println(now);
        LocalDate today = now.toLocalDate();

        // 20世纪初
        LocalDate birthday = LocalDate.of(1900, 1, 1);
        out.println(birthday);

        long daysToToday = today.toEpochDay() - birthday.toEpochDay();
        out.println(daysToToday);

        // 昨晚
        LocalTime midNight = LocalTime.of(23, 59, 59);
        LocalDateTime lastNight = LocalDateTime.of(today.minusDays(1), midNight);
        out.println(lastNight);

        LocalDate yesterday = lastNight.toLocalDate();
        out.println(yesterday);

        daysToToday = today.toEpochDay() - yesterday.toEpochDay();
        out.println(daysToToday);

        // 使用Duration
        Duration durationToNow = Duration.between(lastNight, now);
        out.println(durationToNow);
        out.println(durationToNow.toDays());
        out.println(durationToNow.toHours());

        // can not use Duration on LocalTime
        //        Duration timeToNow = Duration.between(now, midNight);
        //        out.println(timeToNow);
        //        out.println(timeToNow.toDays());
        //        out.println(timeToNow.toHours());

        // 使用Period
        Period period = Period.between(yesterday, today);
        out.println(period);

        period = Period.between(birthday, today);
        out.println(period);

        // 使用until
        // LocalDate -> LocalDate || LocalDateTime
        out.println(yesterday.until(today, ChronoUnit.DAYS)); // 1
        out.println(yesterday.until(now, ChronoUnit.DAYS)); // 1
        //        out.println(yesterday.until(today, ChronoUnit.HOURS)); // Unsupported unit: Hours
        //        out.println(yesterday.until(now, ChronoUnit.HOURS)); // Unsupported unit: Hours
        // LocalDateTime -> LocalDate. Not Supported
        //        out.println(lastNight.until(today, ChronoUnit.DAYS)); // error
        // LocalDateTime -> LocalDateTime
        out.println(lastNight.until(now, ChronoUnit.DAYS)); // 0
        out.println(lastNight.until(now, ChronoUnit.HOURS)); // 正数
        // LocalTime -> LocalDateTime || LocalTime
        out.println(midNight.until(now, ChronoUnit.HOURS)); // 负数
        out.println(midNight.until(now.toLocalTime(), ChronoUnit.HOURS)); // 负数，结果同上

    }
}
