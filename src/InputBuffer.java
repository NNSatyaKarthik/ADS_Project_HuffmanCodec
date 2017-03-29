import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * Created by nagasaty on 3/28/17.
 */
public class InputBuffer {
// circular buffer.
    private StringBuilder residueBuilder;
    
    public InputBuffer() {
        this.residueBuilder = new StringBuilder();
    }
    
    public String getNextItem(TreeNode root, DataInputStream dis) {
        //TODO update teh offset
        TreeNode pointer = root;
        // process residue.. first
        String res;
        if(residueBuilder.length() == 0){
            try {
                residueBuilder = updateResidueBuilder(dis);
            }catch (EOFException e){
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        for(int i  = 0 ; i  < residueBuilder.length(); i++){
            if(residueBuilder.charAt(i) == '1'){
                if(pointer.right!= null && pointer.right instanceof LeafNode){
                    // end of string
                    res = residueBuilder.substring(0, i+1);
                    residueBuilder = residueBuilder.delete(0, i+1);
                    return res;
                }
                if(i == residueBuilder.length() -1){
                    try {
                        updateResidueBuilder(dis);
                    } catch (EOFException e) {
                        return null;
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                pointer =  pointer.right;
            }else {
                if(pointer.left!= null && pointer.left instanceof LeafNode){
                    // end of string
                    res = residueBuilder.substring(0, i+1);
                    residueBuilder = residueBuilder.delete(0, i+1);
                    return res;
                }
                if(i == residueBuilder.length() -1){
                    try {
                        updateResidueBuilder(dis);
                    } catch (EOFException e) {
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                pointer = pointer.left;
            }
        }
        return "";
    }

    private StringBuilder updateResidueBuilder(DataInputStream dis) throws EOFException, IOException {
        String str = Integer.toBinaryString(dis.readByte());
        if(str.length() < 8)
            for(int i = 0 ; i < (8-str.length()); i++) residueBuilder.append('0');
        else if(str.length() > 8){
            str = str.substring(str.length()-8);
        }
        residueBuilder.append(str);
        return residueBuilder;
    }
}
