package interrupting;

import java.util.concurrent.TimeUnit;

public class SleepAfterThreadWasInterrupted {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception was thrown when interrupted thread tried to sleep");
                e.printStackTrace();
            }
        });
        t.start();
    }
}
