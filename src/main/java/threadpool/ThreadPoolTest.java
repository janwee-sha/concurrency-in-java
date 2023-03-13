package threadpool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public class ThreadPoolTest {
    private ScheduledExecutorService scheduledPool;

    @BeforeEach
    public void setup() {
        scheduledPool = Executors.newScheduledThreadPool(5);
    }

    @AfterEach
    public void cleanup() {
        scheduledPool.shutdown();
        scheduledPool = null;
    }

    @Test
    public void testSchedule() {
        Callable<Double> callable = () -> 639873.2 / 3;
        long startBy = System.currentTimeMillis();
        Future<Double> future = scheduledPool.schedule(callable, 2, SECONDS);
        while (!future.isDone()) {
            continue;
        }
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long duration = System.currentTimeMillis() - startBy;
        assertThat(duration).isGreaterThanOrEqualTo(2000);
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(5);
        scheduledPool.scheduleWithFixedDelay(() -> System.out.println("Fixed rate scheduled"), 500,
                2000, MILLISECONDS);
    }
}
