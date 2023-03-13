package objectsharing;

public class CountSleep {
    private volatile boolean asleep;

    public void count() {
        while (!asleep)
            countSheep();
    }

    private void countSheep() {
        System.out.println("sheep...");
    }
}
