import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by nagasaty on 3/28/17.
 */
public class Decoder {
    
    private static String inputFileBin = "encoded.bin";
    private static String inputFileCodeTable = "code_table.txt";
    private static String outputFile = "decoded.txt";
    private static String outputFileExpected = "../sample1/sample_input_small.txt";
//    private static String outputFileExpected = "../sample2/sample_input_large.txt";
    
    public static void main(String[] args) throws FileNotFoundException {
        HashMap<String, Integer> encodingMap = new HashMap<>();
        System.out.println("Building Map of Codec to Int..");
        Scanner sc = new Scanner(new FileInputStream(inputFileCodeTable));
        String[] line;
        while(sc.hasNextLine()){
            line = sc.nextLine().split(" ");
            encodingMap.put(line[1], Integer.parseInt(line[0]));
        }
        sc.close();

        System.out.println("Constructing Decode Tree..");
        TreeNode root = new TreeNode();
        constructDecodeTree(root, encodingMap); // root is populated with the tree
        
        StringBuilder sb = new StringBuilder();
//        printTree(root, sb);

        System.out.println("Deocding data..");
        //TODO read binary file byte by byte.. and then write it to the output file  which should be same as input file.
        decodeData(root, inputFileBin, outputFile, encodingMap);
        System.out.println("Code Decoded & Written to decode.txt");
        // consistency check..
        System.out.print("Checking Consistency.......");
        if(!consistencyCheck(outputFileExpected, outputFile)){
            System.err.println("failed.. output expected and actual are not equal...");
        }else{
            System.out.println("PASS..");
        }
    }

    private static boolean consistencyCheck(String outputFileExpected, String outputFile) {
        try(Scanner scExpected = new Scanner(Files.newInputStream(Paths.get(outputFileExpected)));
        Scanner scActual = new Scanner(Files.newInputStream(Paths.get(outputFile)))) {
            while(scActual.hasNext() && scExpected.hasNext()){
                if(!scActual.nextLine().trim().equals(scExpected.nextLine().trim())) return false;
            }
            return scActual.hasNext() == scExpected.hasNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void decodeData(TreeNode root, String inputFileBin, String outputFile, HashMap<String, Integer> encodingMap) {
        InputBuffer input= new InputBuffer(); // change here the ccapacity to read bytes
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Paths.get(inputFileBin))); 
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFile)))) {
            String item;
            while((item = input.getNextItem(root, dis))!= null){
                bw.write(encodingMap.get(item)+"\n");
            }
            dis.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printTree(TreeNode root, StringBuilder prefixBuilder) {
        if(root == null) return;
        if(root.left == null && root.right == null) {
            System.out.format("%d %s\n", ((LeafNode)root).getValue(), prefixBuilder.toString());
//            encodingMap.put(((LeafNode)root).getValue(), prefixBuilder.toString());
        }
        prefixBuilder.append("0");
        printTree(root.left, prefixBuilder);
        prefixBuilder.deleteCharAt(prefixBuilder.length()-1);
        prefixBuilder.append("1");
        printTree(root.right, prefixBuilder);
        prefixBuilder.deleteCharAt(prefixBuilder.length()-1);
    }
    
    private static void constructDecodeTree(TreeNode root, HashMap<String, Integer> encodingMap) {
        // String is in binary format.
        TreeNode pointer;
        for(String key : encodingMap.keySet()){
            pointer = root;
            for(int i = 0 ; i  < key.length(); i++){
                if(key.charAt(i) == '0') {
                    if(pointer.left == null){
                        if(i != key.length()-1) pointer.left = new TreeNode();
                        else pointer.left = new LeafNode(encodingMap.get(key));
                    }
                    pointer = pointer.left;
                }
                else {
                    if(pointer.right == null){
                        if(i != key.length()-1) pointer.right= new TreeNode();
                        else pointer.right = new LeafNode(encodingMap.get(key));
                    }
                    pointer = pointer.right;
                }
            }
        }
    }
}
