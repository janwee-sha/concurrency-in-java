package callablefuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Integer> future = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
                return 1;
            } catch (InterruptedException e) {
                return -1;
            }
        });
        future.cancel(true);
        if (!future.isCancelled())
            System.out.println(future.get());
    }
}
