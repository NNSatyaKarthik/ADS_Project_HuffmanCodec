/**
 * Created by nagasaty on 3/28/17.
 */

public class LeafNode extends TreeNode{
    int value;

    LeafNode(int data, int value){
        super(data);
        this.value = value;
    }

    LeafNode(int value){
        super(0);
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}

