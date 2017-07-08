package runners;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs all tests in parallel and waits for them to complete.
 * <p>并行执行test方法，每个test方法一个Thread，最多{@link MAX_THREADS}个test方法同时执行
 */
public class ParallelTestClassRunner extends BlockJUnit4ClassRunner {


    private AtomicInteger numThreads;

    public static int MAX_THREADS = 10;

    public ParallelTestClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        numThreads = new AtomicInteger(0);
    }

    // Runs the test corresponding to child，which can be assumed to be an element of the list returned by getChildren()
    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        while (numThreads.get() > MAX_THREADS) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Interrupted: " + method.getName());
                e.printStackTrace();
                return; // The user may have interrupted us; this won't happen normally
            }
        }
        numThreads.incrementAndGet();
        // 用线程执行父类runChild（method, notifier）
        new Thread(new Test(method, notifier)).start();
    }

    // childrenInvoker() call runChild(Object, RunNotifier) on each object returned by getChildren()
    // evaluate() run the action, 调用父类BlockJUnit4ClassRunner的evaluate()
    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                ParallelTestClassRunner.super.childrenInvoker(notifier).evaluate();
                // wait for all child threads (tests) to complete
                while (numThreads.get() > 0) {
                    Thread.sleep(1000);
                }
            }
        };
    }

    class Test implements Runnable {
        private final FrameworkMethod method;
        private final RunNotifier notifier;

        public Test(final FrameworkMethod method, final RunNotifier notifier) {
            this.method = method;
            this.notifier = notifier;
        }

        @Override
        public void run() {
            System.err.println(method.getName());
            ParallelTestClassRunner.super.runChild(method, notifier);
            numThreads.decrementAndGet();
        }
    }

}
