import org.junit.jupiter.api.Test;

/**
 * Created by nagasaty on 3/29/17.
 */
public class PairingHeapTest {
    @Test
    public void offer() throws Exception {
        PairingHeap ph = new PairingHeap();
        for (int i = 0; i < 10; i++) {
            ph.offer(i%3);
        }
        for(int i = 0 ; i < 10; i++){
            System.out.println(ph.poll());
        }
    }

    @Test
    public void poll() throws Exception {

    }

}