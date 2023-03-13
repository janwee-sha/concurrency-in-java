package stateofthread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StateOfThread {
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t0 = new Thread(() -> {
            System.out.println("This is thread 0");
            try {
                System.out.println("Thread 0 got lock");
            } finally {
                lock.unlock();
            }
        });
        Thread t1 = new Thread(() -> {
            System.out.println("This is thread 1");
            if (lock.tryLock()) {
                try {
                    System.out.println("Thread 1 got lock");
                    TimeUnit.MILLISECONDS.sleep(600);
                } catch (InterruptedException e) {
                    System.out.println("Thread 1 interrupted");
                } finally {
                    lock.unlock();
                }
            }
        });
        System.out.println(t0.getState());
        t1.start();
        TimeUnit.MILLISECONDS.sleep(100);
        t0.start();
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(t0.getState());
        t0.sleep(100);
        System.out.println(t0.getState());
    }
}
