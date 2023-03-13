package taskexecution;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallerRecursive {
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(10);

    public <T> void parallerRecursive(final Executor exec, List<Node<T>> nodes, final Collection<T> results) {
        for (final Node<T> node : nodes) {
            EXEC.execute(() -> results.add(node.val));
            parallerRecursive(exec, node.children, results);
        }
    }
}

class Node<T> {
    T val;
    List<Node<T>> children;
}

