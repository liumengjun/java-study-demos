import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Created by liumengjun on 2023-11-17.
 */
public class VirtualThreadsDemo {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        virtualThreadsCase();
//        platformThreadsCase();
        long end = System.currentTimeMillis();
        System.out.println("elapsed time: " + (end - start) + "ms");
    }

    private static void virtualThreadsCase() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    display(i);
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }

    private static void platformThreadsCase() {
        try (var executor = Executors.newFixedThreadPool(1000)) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    display(i);
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }

    private static void display(int flag) {
        var t = Thread.currentThread();
        var p = t.isVirtual() ? "Virtual" : "Platform";
        var g = t.getThreadGroup();
        var c = g.activeCount();
        // --enable-preview
//        var info = StringTemplate.STR."\{flag} in \{g} \{c} \{p} Thread[\{t.getId()}]";
        System.out.println(flag + " in " + g + " " + c + " " + p + " Thread[" + t.getId() + "]");
    }
}
