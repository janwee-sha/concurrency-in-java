package interrupting;

import annotation.DeadLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@DeadLock
public class SuspendThread {
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            if (lock.tryLock()) {
                try {
                    System.out.println("Thread 0 got lock");
                    TimeUnit.MILLISECONDS.sleep(600);
                } catch (InterruptedException e) {
                    System.out.println("Thread 0 interrupted");
                } finally {
                    lock.unlock();
                }
            }
        });
        t.start();
        TimeUnit.MILLISECONDS.sleep(300);
        t.suspend();
        if (lock.tryLock()) {
            try {
                System.out.println("Thread main got lock");
                t.resume();
            } finally {
                lock.unlock();
            }
        }
    }
}
