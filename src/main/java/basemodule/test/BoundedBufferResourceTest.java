package basemodule.test;

import basemodule.BoundedBuffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoundedBufferResourceTest {
    private static final int CAPACITY = 10, THRESHOLD = 10;

    @BeforeAll
    static void beforeAll() {
        System.out.println(">>> Starting BoundedBufferResourceTest.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println(">>> Finished BoundedBufferResourceTest.");
    }

    @Test
    public void testLeak() throws InterruptedException {
        System.out.println("Testing if memory will leak after doExtract.");

        BoundedBuffer<Big> bb = new BoundedBuffer<>(CAPACITY);
        long heapSize1 = Runtime.getRuntime().totalMemory();
        for (int i = 0; i < CAPACITY; i++) bb.put(new Big());
        for (int i = 0; i < CAPACITY; i++) bb.take();
        long heapSize2 = Runtime.getRuntime().totalMemory();
        //heapSize1 and heapSize2 will be obviously different if doExtract method don't set items of array to null.
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }

    private static class Big {
        private double[] unused = new double[100000];
    }
}
