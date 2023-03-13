package interrupting;

import annotation.NotThreadSafe;

import java.util.concurrent.TimeUnit;

@NotThreadSafe
public class StopThread {
    public static void main(String[] args) throws InterruptedException {
        Something s = new Something();
        Thread t = new Thread(() -> {
            s.amount = 109;
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.name = "nothing";
        });
        t.start();
        TimeUnit.MILLISECONDS.sleep(300);
        t.stop();
        System.out.println(s);
    }

    private static class Something {
        private String name;
        private int amount;

        @Override
        public String toString() {
            return "Something{" +
                    "name='" + name + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }
}
