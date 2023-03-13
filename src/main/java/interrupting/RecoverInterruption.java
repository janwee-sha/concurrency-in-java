package interrupting;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RecoverInterruption {
    public static void main(String[] args) throws Exception {
        BlockingQueue<Thread> queue = new LinkedBlockingQueue<>();
        Thread task1 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("output from Task 1");
        });
        Thread task2 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("output from Task 2");
        });
        queue.put(task1);
        queue.put(task2);
        while (true)
            getNextTask(queue).start();
    }

    private static Thread getNextTask(BlockingQueue<Thread> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) Thread.currentThread().interrupt();
        }
    }
}
