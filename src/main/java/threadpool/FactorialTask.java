package threadpool;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class FactorialTask extends RecursiveTask<BigInteger> {
    private static final int threshold = 20;
    private int start = 1;
    private int n;

    public FactorialTask(int n) {
        this.n = n;
    }

    public FactorialTask(int start, int n) {
        this.start = start;
        this.n = n;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        BigInteger result = pool.invoke(new FactorialTask(100));
        System.out.println(result);
        pool.shutdown();
    }

    @Override
    protected BigInteger compute() {
        if ((n - start) >= threshold) {
            return invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .reduce(BigInteger.ONE, BigInteger::multiply);
        } else {
            return calculate(start, n);
        }
    }

    private Collection<FactorialTask> createSubtasks() {
        List<FactorialTask> tasks = new ArrayList<>();
        int mid = (start + n) / 2;
        tasks.add(new FactorialTask(start, mid));
        tasks.add(new FactorialTask(mid + 1, n));
        return tasks;
    }

    private BigInteger calculate(int start, int n) {
        return IntStream.rangeClosed(start, n)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }
}
