/**
 * Created by nagasaty on 3/28/17.
 */
public class TreeNode implements Comparable<TreeNode>{
    int data;
    TreeNode left; // 0
    TreeNode right; // 1
    TreeNode(int data){
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public TreeNode() {
        data = 0 ; 
    }
    
    @Override
    public int compareTo(TreeNode o) {
        return (this.data < o.data)?-1:(this.data == o.data)?0:1;
    }
}

