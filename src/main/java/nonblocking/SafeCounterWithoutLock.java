package nonblocking;

import java.util.concurrent.atomic.AtomicInteger;

public class SafeCounterWithoutLock {
    private final AtomicInteger counter = new AtomicInteger(0);

    public int count() {
        return counter.get();
    }

    public void increment() {
        counter.incrementAndGet();
    }
}
