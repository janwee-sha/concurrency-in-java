package threadpool.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SaturationPolicyTest {
    /**
     * Abort policy causes the executor to throw a RejectedExecutionException.
     */
    @Test
    public void testAbort() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, MILLISECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());

        executor.execute(() -> waitFor(250));

        assertThatThrownBy(() -> executor.execute(() -> System.out.println("Will be " +
                "rejected")))
                .isInstanceOf(RejectedExecutionException.class);
        executor.shutdown();
    }

    /**
     * Caller-Runs Policy makes the caller thread execute the task.
     */
    @Test
    public void testCallerRuns() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, MILLISECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        executor.execute(() -> waitFor(250));

        StringBuilder runningThreadName = new StringBuilder();
        executor.execute(() -> {
            runningThreadName.append(Thread.currentThread().getName());
            waitFor(500);
        });
        assertThat(runningThreadName.toString()).isEqualTo(Thread.currentThread().getName());
        executor.shutdown();
    }

    /**
     * The discard policy silently discards the new task when it fails to submit it.
     */
    @Test
    public void testDiscard() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, MILLISECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy());

        executor.execute(() -> waitFor(100));

        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        executor.execute(() -> queue.offer("Discarded Result"));

        assertThat(queue.poll(200, MILLISECONDS)).isNull();
        executor.shutdown();
    }

    /**
     * The discard-oldest policy first removes a task from the head of the queue, then re-submits the new task.
     */
    @Test
    public void testDiscardOldest() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, MILLISECONDS,
                new ArrayBlockingQueue<>(2),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        BlockingQueue<Integer> queue = new LinkedBlockingDeque<>();
        executor.execute(() -> queue.offer(1));
        executor.execute(() -> queue.offer(2));
        executor.execute(() -> queue.offer(3));

        assertThat(queue).contains(2, 3);
        executor.shutdown();
    }


    private void waitFor(long milli) {
        try {
            MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
