/**
 * Created by nagasaty on 3/29/17.
 */
public abstract class Heaps<T> {

    abstract  void offer(T data);

    abstract T poll();

    abstract T peek();
    
    abstract int size();
    
    abstract boolean isEmpty();
}
