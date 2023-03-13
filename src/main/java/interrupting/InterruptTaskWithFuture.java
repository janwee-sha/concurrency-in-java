package interrupting;

import java.util.concurrent.*;

public class InterruptTaskWithFuture {
    private static final ScheduledExecutorService scheExec = Executors.newScheduledThreadPool(10);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        final ThreadPoolExecutor executor=new ThreadPoolExecutor(10,10,
                0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(10));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        Future<Void> task = CompletableFuture.runAsync(r);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            //接下来任务将被取消
        } catch (ExecutionException e) {
            //如果在任务中抛出了异常，那么重新抛出该异常
            throw new RuntimeException(e);
        } finally {
            //如果任务正在运行，那么将被中断
            task.cancel(true);
        }
    }
}
