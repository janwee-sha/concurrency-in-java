package taskexecution;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OutOfTime {
    private static final ScheduledExecutorService EXEC = Executors.newScheduledThreadPool(100);

    public static void main(String[] args) throws Exception {
//        executeWithTimer();
        executeWithScheduledThreadPool();
    }

    private static void executeWithScheduledThreadPool() {
        EXEC.schedule(new ThrowTask(), 1, TimeUnit.MINUTES);
        EXEC.schedule(new ThrowTask(), 1, TimeUnit.MINUTES);
        EXEC.shutdown();
    }

    /**
     * this method will cause thread leakage problem
     */
    private static void executeWithTimer() throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        TimeUnit.SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);
        TimeUnit.SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask {
        public void run() {
            throw new RuntimeException("runtime exception thrown from " + this);
        }
    }
}
