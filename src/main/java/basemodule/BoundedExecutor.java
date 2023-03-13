package basemodule;

import java.util.concurrent.*;

public class BoundedExecutor {
    private final ExecutorService executor;
    private final Semaphore semaphore;

    public BoundedExecutor(ExecutorService executor, int bound) {
        this.executor = executor;
        this.semaphore = new Semaphore(bound);
    }

    public static void main(String[] args) throws Exception {
        BoundedExecutor exec = new BoundedExecutor(Executors.newCachedThreadPool(), 10);
        for (int i = 0; i < 100; i++) {
            int count = i;
            exec.submitTask(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(count);
            });
        }
        exec.shutdown();
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            executor.execute(() -> {
                try {
                    command.run();
                } finally {
                    semaphore.release();
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }
}
