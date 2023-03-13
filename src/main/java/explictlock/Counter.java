package explictlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private final Lock lock = new ReentrantLock();
    private int count = 0;

    public int getNext() {
        if (lock.tryLock()) {
            try {
                return count++;
            } finally {
                lock.unlock();
            }
        }
        return -1;
    }
}
