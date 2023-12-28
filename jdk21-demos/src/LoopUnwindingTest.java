import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 探索循环展开带来的性能提升
 * <p>按照一定的步数跳跃，每次循环处理多个对象，循环次数减少了，循环也变胖了。
 * <p>总数据量很大时，如果循环体内计算简单，性能提升显著，如果循环体很复杂，几乎没有提升。总数据量较小时，如果计算很简单也没有提升，减少的循环次数不明显，而且程序更复杂了。
 * <p>对比多线程处理，循环体内计算越复杂，用多线程性能提升越明显。如果计算很简单，只有数据量很大时才需要用多线程。
 * <p>Created by liumengjun on 2023-12-28.
 */
public class LoopUnwindingTest {
    public static void main(String[] args) {
        int n = 1_0000_0000;
        // ---普通循环求和---
        long start = System.currentTimeMillis();
        long result = sum(n);
        long time = System.currentTimeMillis() - start;
        System.out.println("①Σ" + n + "= " + result + ", time: " + time);
        // ---循环展开求和---
        start = System.currentTimeMillis();
        long result2 = sum2(n);
        long time2 = System.currentTimeMillis() - start;
        System.out.println("②Σ" + n + "= " + result2 + ", time: " + time2);
        if (result2 != result) {
            System.out.printf("Oh god, second result is wrong, %s != %s!\n", result2, result);
            return;
        }
        System.out.printf("result ok, speed: %.2fx\n", (double) time / time2);
        // ---多线程求和---
        start = System.currentTimeMillis();
        long result3 = sum3(n);
        long time3 = System.currentTimeMillis() - start;
        System.out.println("③Σ" + n + "= " + result3 + ", time: " + time3);
        if (result3 != result) {
            System.out.printf("Oh god, second result is wrong, %s != %s!\n", result3, result);
            return;
        }
        System.out.printf("result ok, speed: %.2fx\n", (double) time / time3);
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
     * 循环展开求和
     * <p>按照一定的步数跳跃，每次循环处理多个对象，使循环次数减少，这样性能也许有一定提升。
     * <p>(区别于全部展开，如果n很小比如<10，也许全部展开不用循环就可以了)
     */
    private static long sum2(int n) {
        long result = 0;
        final int gap = 9;  // 尝试寻找最优的`gap`, 2～3性能不好，4以上都差不多。
        int m = n / gap * gap;
        for (int j = 1; j <= m; j += gap) {
            result += calc0(j);
            result += calc0(j + 1);
            result += calc0(j + 2);
            result += calc0(j + 3);
            result += calc0(j + 4);
            result += calc0(j + 5);
            result += calc0(j + 6);
            result += calc0(j + 7);
            result += calc0(j + 8);
//            result += calc0(j + 9);
        }
        for (int j = m + 1; j <= n; j++) {
            result += calc0(j);
        }
        return result;
    }

    /**
     * 对每次循环对象的处理函数，随着计算量的增大，循环展开的加成越来越小。
     * 如果计算太复杂，`N`太大的话就不知道什么时候结束了
     */
    private static long calc0(long j) {
//        return j;
//        return j * 3;
//        return Math.abs(j);
//        return (long) Math.floor(j);
//        return (long) Math.sqrt(j);
        return (long) Math.log1p(j);
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
