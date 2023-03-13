package objectsharing;

public class NoVisibility {
    private static boolean ready;
    private static int number;

    public static void main(String[] args) {
        //可能会永久循环下去
        new ReaderThread().start();
        //下面两段指令可能会发生重排序，导致程序输出结果为0
        number = 42;
        ready = true;
    }

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }
}