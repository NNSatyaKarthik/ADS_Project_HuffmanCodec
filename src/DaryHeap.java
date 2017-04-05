import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by nagasaty on 3/30/17.
 */
public class DaryHeap<T extends Comparable<T>> extends Heaps<T> {
    
    ArrayList<T> heap;
    private int d, kbasedindex;
    
    private final Comparator<? super T> comparator; 
    
    public DaryHeap(int d){
        this.d = d;
        this.heap = new ArrayList<T>();
        comparator = (o1, o2) -> o1.compareTo(o2);
        this.kbasedindex = 0;
    }
    public DaryHeap(int d, int kbasedindex) {
        this.d = d;
        this.heap = new ArrayList<T>();
        comparator = (o1, o2) -> o1.compareTo(o2);
        this.kbasedindex = kbasedindex;
        for (int i = 0; i < this.kbasedindex; i++) {
            this.heap.add(null);
        }
    }
    
    public DaryHeap(int d, Comparator<? super T> c){
        this.d = d;
        this.heap = new ArrayList<T>();
        this.comparator = c;
        this.kbasedindex = 0;
    }
    
    public DaryHeap(int d, int kbasedindex, Comparator<? super T> c){
        this.d = d;
        this.heap = new ArrayList<T>();
        this.comparator = c;
        this.kbasedindex = kbasedindex;
        for (int i = 0; i < this.kbasedindex; i++) {
            this.heap.add(null);
        }
    }
    
    @Override
    void offer(T data) {
        this.heap.add(data);
        if(this.heap.size() > (this.kbasedindex+1)){
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
        index -= this.kbasedindex;
        if(index <= 0) return -1;
        return ((index-1)/ this.d) + this.kbasedindex;
    }
    
    private void heapifyUp(int i) {
        int parentId = this.getParentId(i);
        if(parentId != -1){
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
        T res = this.heap.get(this.kbasedindex); // take the first element.. which is root
        this.heap.set(this.kbasedindex, this.heap.get(this.heap.size()-1)); // set the root with the last element
        this.heap.remove(this.heap.size()-1);// remove the last element
        heapifyDown(this.kbasedindex);
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
                int childId = getIthChildId(index, i);
                if(childId < this.heap.size()){
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

    private int getIthChildId(int index, int i) {
        index = index - this.kbasedindex;
        return ((index * this.d) + i) + this.kbasedindex;
    }

    @Override
    T peek() {
        if(this.isEmpty()) throw new NullPointerException("Heap is Empty");
        return this.heap.get(kbasedindex);
    }

    @Override
    int size() {
        return this.heap.size() - this.kbasedindex;
    }

    @Override
    boolean isEmpty() {
        return (!this.heap.isEmpty()) && (this.size() == 0);
    }
}
