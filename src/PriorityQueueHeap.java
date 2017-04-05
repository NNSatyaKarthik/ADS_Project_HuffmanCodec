import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by nagasaty on 3/29/17.
 */
public class PriorityQueueHeap<T extends Comparable<T>> extends Heaps {
    
    PriorityQueue<T> queue;
    
    public PriorityQueueHeap() {
        this.queue = new PriorityQueue<>((a, b) -> a.compareTo(b));
    }
    
    @Override
    void offer(Object data) {
        this.queue.offer((T)data);    
    }

    @Override
    T poll() {
        return this.queue.poll();
    }

    @Override
    T peek() {
        return this.queue.peek();
    }

    @Override
    int size() {
        return this.queue.size();
    }

    @Override
    boolean isEmpty() {
        return this.queue.isEmpty();
    }

}
