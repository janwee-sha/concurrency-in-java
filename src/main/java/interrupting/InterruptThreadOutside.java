package interrupting;

import annotation.Terrible;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * interrupt a thread by its caller
 */
@Terrible
public class InterruptThreadOutside {
    private static final ScheduledExecutorService scheExec = Executors.newScheduledThreadPool(10);

    //timeRun可以在任意一个线程中调用，因此它无法知道这个调用线程的中断策略，所以这种方式破坏了规则
    //这么做可能导致中断timeRun所在的线程的取消任务在timeRun返回到调用者之后启动
    public static void timedRun(Runnable r, long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        scheExec.schedule(taskThread::interrupt, timeout, unit);
        r.run();
    }
}
