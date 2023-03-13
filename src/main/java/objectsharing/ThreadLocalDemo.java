package objectsharing;

import java.util.concurrent.TimeUnit;

public class ThreadLocalDemo {
    private static final ThreadLocal<Integer> var = ThreadLocal.withInitial(() ->
            -1);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println(var.get());
            var.set(1);
            System.out.println(var.get());
        }).start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(() -> {
            System.out.println(var.get());
            var.set(2);
            System.out.println(var.get());
        }).start();
    }
}
