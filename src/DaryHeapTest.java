import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by nagasaty on 3/30/17.
 */

public class DaryHeapTest {
    
    @Test
    public void testBinary(){
        DaryHeap<Integer> h = new DaryHeap<>(2);
        h.offer(3);
        h.offer(2);
        
        System.out.println(h.poll());
        h.offer(15);
        h.offer(5);
        h.offer(4);
        h.offer(45);
        System.out.println(h.poll());
    }


    
    @Test
    public void test4ary(){
        PriorityQueue<Integer> pq = new PriorityQueue<>(10, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });
        DaryHeap<Integer> h = new DaryHeap<Integer>(4, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1<o2)?1:(o1==o2)?0:-1;
            }
        });
        h.offer(3);
        h.offer(2);

        System.out.println(h.poll());
        h.offer(15);
        h.offer(5);
        h.offer(4);
        h.offer(45);
        System.out.println(h.poll());
        System.out.println(h.poll());
        System.out.println(h.poll());
        System.out.println(h.poll());
        
    }
    
}