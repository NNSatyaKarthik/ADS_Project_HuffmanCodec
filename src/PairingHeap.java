import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by nagasaty on 3/29/17.
 */
class Node<T extends Comparable<T>> implements Comparable<Node>{
    
    T data;
    Node child;
    Node left, right;

    public Node(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.child = null;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public int compareTo(Node node) {
//        return (this.data < node.data)?-1:(this.data == node.data)?0:1;
        return this.data.compareTo((T) node.data);
    }

    public void insert(Node node) {
        if(this.child == null){
            this.child = node;
            node.left = this;
            node.right = null;
        }else{
            this.child.left = node;
            node.right = this.child;
            node.left = this;
            this.child = node;
        }
    }
}

public class PairingHeap<T extends Comparable<T>> extends Heaps<T>{
    
    private int size;
    
    Node<T> root;
    
    public void offer(T data){
        Node node = new Node(data);
        this.size++;
        if(this.root == null){
            this.root = node;
        }else{
            if(this.root.compareTo(node) == 1){
                node.insert(this.root);
                this.root = node;
            }else{
                this.root.insert(node);
            }
        }   
    }
    
    public T poll(){
        if(this.root == null) throw new NullPointerException("No root found");
        T res;
        res = this.root.data;
        if(this.root.child == null){
            this.root = null;
        }else{
            this.root = meld(this.root.child);
        }
        this.size--;
        return res;
    }

    @Override
    T peek() {
        return this.root.data;
    }

    private Node meld(Node list) {
        ArrayList<Node> childList = new ArrayList<>();
        Node ptr = list, left, right, next;
        //pass1
        while(ptr != null){
            left =  ptr;
            if(left.right != null){
                right = left.right;
                next = right.right;
                
                right.left = null; 
                right.right = null;
                left.right = null;
                left.left = null;
                
                if(left.compareTo(right) == -1){
                    left.insert(right);
                    childList.add(left);
                }else{
                    right.insert(left);
                    childList.add(right);
                }
            }else{
                next = left.right;
                
                left.right = null;
                left.left = null;

                childList.add(left);
            }
            ptr = next;   
        }
        
        //pass2
        while(childList.size() > 1){
            left = childList.remove(0);
            right = childList.remove(0);
            if(left.compareTo(right) == -1){
                left.insert(right);
                childList.add(0, left);
            }else{
                right.insert(left);
                childList.add(0, right);
            }
        }
        return childList.remove(0);
    }
    
    public int size() {
        return this.size;
    }

    @Override
    boolean isEmpty() {
        return this.size == 0;
    }
}
