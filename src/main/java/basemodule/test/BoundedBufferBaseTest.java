package basemodule.test;

import basemodule.BoundedBuffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BoundedBufferBaseTest {

    private static final long LOCKUP_DETECT_TIMEOUT = 1000;

    @BeforeAll
    static void beforeAll() {
        System.out.println(">>> Starting BoundedBufferBaseTest.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println(">>> Finished BoundedBufferBaseTest.");
    }

    @Test
    public void testIsEmptyWhenConstructed() {
        System.out.println("Testing if BoundedBuffer is empty when constructed.");

        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    @Test
    public void testIsFullAfterPuts() throws InterruptedException {
        System.out.println("Testing if BoundedBuffer is full after putting elements..");

        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++) bb.put(i);
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    @Test
    public void testTakeBlocksWhenEmpty() {
        System.out.println("Testing if it takes blocks after emptying BoundedBuffer.");

        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        Thread taker = new Thread(() -> {
            try {
                int unused = bb.take();
                fail();//如果执行到这里，那么表示出现了一个错误
            } catch (InterruptedException e) {
                //do nothing to make test pass.
            }
        });
        try {
            taker.start();
            TimeUnit.MILLISECONDS.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }
}
