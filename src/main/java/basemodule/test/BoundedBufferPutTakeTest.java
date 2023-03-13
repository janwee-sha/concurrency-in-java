package basemodule.test;

import basemodule.BoundedBuffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BoundedBufferPutTakeTest {
    private static final ExecutorService pool =
            Executors.newCachedThreadPool();
    private static final int N_PAIRS = 10, N_TRAILS = 1000;
    private static final CyclicBarrier BARRIER = new CyclicBarrier(N_PAIRS * 2 + 1);
    private static final AtomicInteger PUT_SUM = new AtomicInteger(), TAKE_SUM = new AtomicInteger();
    private static final BoundedBuffer<Integer> BUFF = new BoundedBuffer<>(10);

    @BeforeAll
    static void beforeAll() {
        System.out.println(">>> Starting BoundedBufferPutTakeTest.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println(">>> Finished BoundedBufferPutTakeTest.");
    }

    //simply use xor operation to generate random numbers
    private static int xorShift(final int num) {
        int result = num;
        result ^= (result << 6);
        result ^= (result >>> 21);
        result ^= (result << 7);
        return result;
    }

    @Test
    public void testPutAndTake() {
        System.out.println("Testing put and take.");

        try {
            for (int n = 0; n < N_PAIRS; n++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            BARRIER.await();//wait for the Producer and Consumer threads to be ready.
            BARRIER.await();//wait for the Producer and Consumer threads to be finished.
            assertEquals(PUT_SUM.get(), TAKE_SUM.get());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            pool.shutdown();
        }
    }

    private static class Producer implements Runnable {
        public void run() {
            try {
                int seed = this.hashCode() ^ (int) System.nanoTime();
                int sum = 0;
                BARRIER.await();
                for (int n = 0; n < N_TRAILS; n++) {
                    sum += seed;
                    BUFF.put(seed);
                    seed = xorShift(seed);
                }
                PUT_SUM.getAndAdd(sum);
                BARRIER.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Consumer implements Runnable {
        public void run() {
            try {
                int sum = 0;
                BARRIER.await();
                for (int n = 0; n < N_TRAILS; n++) {
                    sum += BUFF.take();
                }
                TAKE_SUM.getAndAdd(sum);
                BARRIER.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
