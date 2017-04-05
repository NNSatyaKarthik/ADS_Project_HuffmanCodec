import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by nagasaty on 3/30/17.
 */
public class DaryHeap<T extends Comparable<T>> extends Heaps<T> {
    
    ArrayList<T> heap;
    private int d;
    private final Comparator<? super T> comparator; 
    
    public DaryHeap(int d) {
        this.d = d;
        this.heap = new ArrayList<T>();
        comparator = (o1, o2) -> o1.compareTo(o2);
    }
    
    public DaryHeap(int d, Comparator<? super T> c){
        this.d = d;
        this.heap = new ArrayList<T>();
        this.comparator = c;
    }

    @Override
    void offer(T data) {
        this.heap.add(data);
        if(this.heap.size() > 1){
            heapifyUp(this.heap.size()-1); // the element at which the node is inserted
        }
    }

    private void swap(int id1, int id2){
        T temp = this.heap.get(id1);
        this.heap.set(id1, this.heap.get(id2));
        this.heap.set(id2, temp);
        return;
    }
    
    private int getParentId(int index){
        if(index <= 0) return -1;
        return (index-1)/ this.d;
    }
    
    private void heapifyUp(int i) {
        int parentId = this.getParentId(i);
        T parent = this.heap.get(parentId), curr = this.heap.get(i);
        while(this.comparator.compare(parent, curr) != -1) { // check if it is -1 or 1
            swap(i, parentId);
            // re initialize the sutff.. 
            i = parentId; //.. set the current element to parent
            curr = this.heap.get(i); // change curr according to I 
            parentId = this.getParentId(i); /// get the new parent eleemnt
            if (parentId == -1)
                break; // if parent id is -1.. we reached the root..already.. no further traversal required
            parent = this.heap.get(parentId); // otherwise .. get the new parent reference
        }
    }

    private int getChildCount(int index){
        int res =0;
        for(int i = 1; i <= this.d; i++){
            if((this.d * index + i) < this.heap.size())res++;
            else break;
        }
        return res;
    }
    
    @Override
    T poll() {
        if(this.isEmpty()) throw new NullPointerException("Heap is empty..");
        T res = this.heap.get(0); // take the first element.. which is root
        this.heap.set(0, this.heap.get(this.size()-1)); // set the root with the last element
        this.heap.remove(this.size()-1);// remove the last element
        heapifyDown(0);
        return res;
    }

    private void heapifyDown(int index) {
        T curr;
        
        while(index < this.heap.size()){
            curr = this.heap.get(index);
            // populate bestChildID.. for min heap.. this should return min element.. and for max this should return max element
            //among the children.
            int bestChildId = -1;
            for(int i = 1 ; i <= this.d; i++){
                int childId = index * this.d + i;
                if(childId < this.size()){
                    if(bestChildId == -1) bestChildId = childId;
                    else{
                        if(this.comparator.compare(this.heap.get(bestChildId), this.heap.get(childId))!= -1){// recheck if this is the correct strategy or not
                            bestChildId = childId;
                        }
                    }
                }else{
                    break;
                }
            }
            
            if(bestChildId == -1) {
                // all childs satisfies the condition.. 
                break;
            }
            if(this.comparator.compare(curr, this.heap.get(bestChildId)) != -1){
                swap(index, bestChildId);
            }else{
                break;
            }
            index = bestChildId;
        }
    }

    @Override
    T peek() {
        if(this.isEmpty()) throw new NullPointerException("Heap is Empty");
        return this.heap.get(0);
    }

    @Override
    int size() {
        return this.heap.size();
    }

    @Override
    boolean isEmpty() {
        return this.heap.isEmpty();
    }
}
