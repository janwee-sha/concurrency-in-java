package nonblocking;

import annotation.NotThreadSafe;

@NotThreadSafe
public class UnsafeCounter {
    private int counter = 0;

    public int count() {
        return counter;
    }

    public void increment() {
        counter++;
    }
}
