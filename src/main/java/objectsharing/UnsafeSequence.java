package objectsharing;

import annotation.NotThreadSafe;

@NotThreadSafe
public class UnsafeSequence {
    private int val;

    public int getNext() {
        return val++;
    }
}
