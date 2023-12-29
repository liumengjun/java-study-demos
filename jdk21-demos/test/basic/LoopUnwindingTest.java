package basic;  // 使用`jmh`必须有包

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * 探索循环展开带来的性能提升
 * <p>按照一定的步数跳跃，每次循环处理多个对象，循环次数减少了，循环也变胖了。
 * <p>如果循环体内计算简单，性能提升显著，如果循环体越复杂，提升越弱。
 * <p>对比多线程处理，循环体内计算越复杂，用多线程性能提升越明显。如果计算很简单，只有数据量很大时才需要用多线程。
 * <pre>
 * 相关参数(都不启用)
 * -XX:LoopUnrollLimit=1
 * -XX:+UnlockDiagnosticVMOptions -XX:-UseCompressedOops -XX:PrintAssemblyOptions=intel
 * -XX:GuaranteedSafepointInterval=0
 * -XX:+UseCountedLoopSafepoints
 * -XX:CompileCommand=print,basic.LoopUnwindingTest::sum
 * </pre>
 * <pre>
 * 测试结果
 * Benchmark                                 Mode  Cnt   Score     Error  Units
 * LoopUnwindingTest.bsum0Common             avgt    3  39.389 ± 117.822  ms/op
 * LoopUnwindingTest.bsum1LongIndex          avgt    3  62.774 ±   0.595  ms/op
 * LoopUnwindingTest.bsum2ManuallyUnrolling  avgt    3  10.867 ±   0.631  ms/op
 * LoopUnwindingTest.bsum3MultiThread        avgt    3  10.272 ±   0.818  ms/op
 * </pre>
 * <p>Created by liumengjun on 2023-12-28.
 */
@Fork(value = 1, jvmArgsPrepend = "-Xmx512m")
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LoopUnwindingTest {

    static final int N = 1_0000_0000;

    public static void main(String[] args) throws RunnerException {
        if (!main0(args)) {  // only check
            return;
        }
        Options opt = new OptionsBuilder()
                .include(LoopUnwindingTest.class.getSimpleName())
                .detectJvmArgs()
                .build();
        new Runner(opt).run();
    }

    public static boolean main0(String[] args) {
        int n = 100_0000;
        // ---普通循环求和---
        long start = System.currentTimeMillis();
        long result = sum(n);
        long time = System.currentTimeMillis() - start;
        System.out.println("①Σ" + n + "= " + result + ", time: " + time);
        // ---suml(n, 1)---
        start = System.currentTimeMillis();
        long result1 = suml(n, 1);
        long time1 = System.currentTimeMillis() - start;
        System.out.println("ⓁΣ" + n + "= " + result1 + ", time: " + time1);
        if (result1 != result) {
            System.out.printf("Oh god, the result is wrong, %s != %s!\n", result1, result);
            return false;
        }
        System.out.printf("result ok, speed: %.2fx\n", (double) time / time1);
        // ---循环展开求和---
        start = System.currentTimeMillis();
        long result2 = sum2(n);
        long time2 = System.currentTimeMillis() - start;
        System.out.println("②Σ" + n + "= " + result2 + ", time: " + time2);
        if (result2 != result) {
            System.out.printf("Oh god, the result is wrong, %s != %s!\n", result2, result);
            return false;
        }
        System.out.printf("result ok, speed: %.2fx\n", (double) time / time2);
        // ---多线程求和---
        start = System.currentTimeMillis();
        long result3 = sum3(n);
        long time3 = System.currentTimeMillis() - start;
        System.out.println("③Σ" + n + "= " + result3 + ", time: " + time3);
        if (result3 != result) {
            System.out.printf("Oh god, the result is wrong, %s != %s!\n", result3, result);
            return false;
        }
        System.out.printf("result ok, speed: %.2fx\n", (double) time / time3);
        return true;
    }

    @Benchmark
    public long bsum0Common() {
        return sum(N);
    }

    @Benchmark
    public long bsum1LongIndex() {
        return suml(N, 1);
    }

    @Benchmark
    public long bsum2ManuallyUnrolling() {
        return sum2(N);
    }

    @Benchmark
    public long bsum3MultiThread() {
        return sum3(N);
    }


    /**
     * 普通循环求和
     */
    private static long sum(int n) {
        long result = 0;
        for (int j = 1; j <= n; j++) {
            result += calc0(j);
        }
        return result;
    }

    /**
     * 普通循环求和, 用`long`型作为`for`的索引
     * 效果不好
     */
    private static long suml(int n, int step1) {
        long result = 0;
        for (long j = 1; j <= n; j += step1) {
            result += calc0(j);
        }
        return result;
    }

    /**
     * 循环展开求和
     * <p>按照一定的步数跳跃，每次循环处理多个对象，使循环次数减少，编译器可以充分优化循环内的计算，这样性能应该有一定提升。
     * <p>但是这样代码更复杂了，而且容易写错
     * <p>(区别于全部展开，如果n很小比如<10，也许全部展开不用循环就可以了)
     */
    private static long sum2(int n) {
        long result = 0, result1 = 0, result2 = 0, result3 = 0, result4 = 0, result5 = 0, result6 = 0, result7 = 0, result8 = 0, result9 = 0;
        final int gap = 4;  // 尝试寻找最优的`gap`, 当为Runtime.getRuntime().availableProcessors()最优
        int m = n / gap * gap;
        for (int j = 1; j <= m; j += gap) {
            result += calc0(j);
            result1 += calc0(j + 1);
            result2 += calc0(j + 2);
            result3 += calc0(j + 3);
//            result4 += calc0(j + 4);
//            result5 += calc0(j + 5);
//            result6 += calc0(j + 6);
//            result7 += calc0(j + 7);
//            result8 += calc0(j + 8);
//            result9 += calc0(j + 9);
        }
        // 如果n不被gap整除，处理剩余部分
        for (int j = m + 1; j <= n; j++) {
            result += calc0(j);
        }
        return result + result1 + result2 + result3 + result4 + result5 + result6 + result7 + result8 + result9;
    }

    /**
     * 对每次循环对象的处理函数，随着计算量的增大，循环展开的加成越来越小。
     * 如果计算太复杂，`N`太大的话就不知道什么时候结束了
     */
    private static long calc0(long j) {
//        return j;
        return j * 3;
//        return Math.abs(j);
//        return (long) Math.floor(j);
//        return (long) Math.sqrt(j);
//        return (long) Math.log1p(j);
//        return (long) (Math.sin(j) * 1000);
    }

    /**
     * 多线程求和
     */
    private static long sum3(int n) {
        List<Future<Long>> futures = new ArrayList<>();
        final int workerCount = Runtime.getRuntime().availableProcessors();
//        try (var executor = Executors.newFixedThreadPool(workerCount)) {  // 固定线程数
//        try (var executor = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {  // 每个任务一个线程
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {  // 虚拟线程
            int batchSize = (n + workerCount - 1) / workerCount;
            for (int i = 0; i < workerCount; i++) {
                int start = i * batchSize + 1, end = Math.min(start + batchSize - 1, n);
//                System.out.printf("sum: [%s, %s]\n", start, end);
                var future = executor.submit(new SectionSumWorker(start, end));
                futures.add(future);
            }
            // 获取任务的结果
            long result = 0;
            for (var future : futures) {
                result += future.get();
            }
            return result;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    record SectionSumWorker(int start, int end) implements Callable<Long> {
        @Override
        public Long call() throws Exception {
//            System.out.printf("sum: [%s, %s]\n", start, end);
            long result = 0;
            for (int j = start; j <= end; j++) {
                result += calc0(j);
            }
            return result;
        }
    }
}
