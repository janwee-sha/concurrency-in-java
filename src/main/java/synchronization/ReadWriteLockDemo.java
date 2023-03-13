package synchronization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    private double totalBalance;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        demo.setTotalBalance(100);
        new Thread(() -> {
            demo.setTotalBalance(200);
            System.out.println("Thread 0: total balance is " + demo.getTotalBalance());
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            System.out.println("Thread 1: total balance is " + demo.getTotalBalance());
        }).start();
    }

    public double getTotalBalance() {
        readLock.lock();
        try {
            return totalBalance;
        } finally {
            readLock.unlock();
        }
    }

    public void setTotalBalance(double totalBalance) {
        writeLock.lock();
        try {
            this.totalBalance = totalBalance;
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }
    }
}
