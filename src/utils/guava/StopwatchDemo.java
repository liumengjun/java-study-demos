package utils.guava;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by liumengjun on 2017-09-15.
 */
public class StopwatchDemo {

    public static void main(String[] args) throws InterruptedException {
        /**
         * Stopwatch，该类是guava包内的，spring和apache commons都有同名的工具类
         */
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(1000);
        stopwatch.stop();
        System.out.println("耗时: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    }
}
