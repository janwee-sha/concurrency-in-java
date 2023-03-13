package interrupting;

import annotation.NotBest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * interrupt a task in a particular thread
 */
@NotBest
public class InterruptTaskParticularly {
    private static final ScheduledExecutorService scheExec = Executors.newScheduledThreadPool(10);

    //无法知道执行线程是因为线程正常退出而返回还是join超时而返回
    public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowableTask implements Runnable {
            private volatile Throwable t;

            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            private void rethrow() {
                if (t != null) throw new RuntimeException(t);
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        scheExec.schedule(taskThread::interrupt, timeout, unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }
}
