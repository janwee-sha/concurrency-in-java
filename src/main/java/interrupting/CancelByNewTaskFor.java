package interrupting;

import annotation.ThreadSafe;

import java.util.concurrent.*;

interface CancallableTask<T> extends Callable<T> {
    void cancel();

    RunnableFuture<T> newTask();
}

public class CancelByNewTaskFor {

}

@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor {
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return (callable instanceof CancallableTask) ?
                ((CancallableTask<T>) callable).newTask() : super.newTaskFor(callable);
    }
}


