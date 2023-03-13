package nonblocking.test;

import nonblocking.SafeCounterWithoutLock;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SafeCounterWithoutLockTest {
    private static final int N_TASKS = 1000;
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(N_TASKS);

    @Test
    public void test() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(1 << 30);
        SafeCounterWithoutLock counter = new SafeCounterWithoutLock();
        CountDownLatch latch = new CountDownLatch(N_TASKS);
        Runnable runnable = () -> {
            counter.increment();
            latch.countDown();
        };
        for (int i = 0; i < N_TASKS; i++) {
            EXEC.submit(runnable);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(N_TASKS, counter.count());
    }
}
