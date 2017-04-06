import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by nagasaty on 3/28/17.
 */

public class encoder {
//    private static String inputFile = "sample_input_small.txt";
//    private static String inputFile = "../sample2/sample_input_large.txt";
    private static String inputFile = "src/input.txt";
    private static String outputFileBin = "encoded.bin";
    private static String outputFileCodeTable = "code_table.txt";
    
    public static void main(String[] args) throws FileNotFoundException {
        long encodingStart = System.currentTimeMillis();
        if(args.length != 1){
            System.err.println("Args length should be 1.. found..: "+args.length);
            System.exit(1);
        }else inputFile = args[0];
        HashMap<Integer, Integer> fmap = new HashMap<>();
        Scanner sc = new Scanner(new FileInputStream(inputFile));
        int key;
        while(sc.hasNext()){
            key = Integer.parseInt(sc.nextLine());
            fmap.put(key, fmap.getOrDefault(key, 0) + 1);
        }
        sc.close();

        System.out.println("Freq Map Built");
        TreeNode root = null;
//        root = performanceAnalysis(fmap, root);
        root = constructHuffmanTree(fmap, 0);

        
        System.out.println("Huffman Tree Built");

        Map<Integer, String> encodingMap = getMap(root);
        System.out.print("Writing Binary and CodeTable.txt to the path.....");
        writeToOutputFile(inputFile, outputFileBin, outputFileCodeTable, encodingMap);
        System.out.println("Encoding Done!");
        System.out.println("Total Time taken : "+ (System.currentTimeMillis() - encodingStart)/1000.0);
    }

    private static TreeNode performanceAnalysis(HashMap<Integer, Integer> fmap, TreeNode root) {
        int iterations = 1;
        long start, end;
        for(int k = 0 ; k  < 10; k++){
            
    //        System.out.print("Time Taken by 4-ary Heap:....");
            start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                root = constructHuffmanTree(fmap, 2);
            }
            end = System.currentTimeMillis();
            System.out.print((end-start)/(1000.0*iterations) + "\t");
            
    //        System.out.print("Time Taken by 4-ary CACHEOPTIMIZED Heap:...");
            start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                root = constructHuffmanTree(fmap, 3);
            }
            end = System.currentTimeMillis();
            System.out.print((end-start)/(1000.0*iterations) + "\t");
    
    //        System.out.print("Time Taken by pairing Heap:...");
            start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                root = constructHuffmanTree(fmap, 0);
            }
            end = System.currentTimeMillis();
            System.out.print((end-start)/(1000.0*iterations) + "\t");
    
            
            //        System.out.print("Time Taken by Binary Heap:...");
            start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                root = constructHuffmanTree(fmap, 1);
            }
            end = System.currentTimeMillis();
            System.out.print((end-start)/(1000.0*iterations) + "\n");
        }
        return root;
    }

    private static Boolean writeToOutputFile(String inputFile, String outputFileBin, String outputFileCodeTable, Map<Integer, String> encodingMap) throws FileNotFoundException {
        return !(!writeCodecTxt(outputFileCodeTable, encodingMap) && !writeBinaryFile(inputFile, outputFileBin, encodingMap));
    }

    private static boolean writeCodecTxt(String outputFileCodeTable, Map<Integer, String> encodingMap) {
        try (BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(Paths.get(outputFileCodeTable)))){
            for (Iterator<Integer> iterator = encodingMap.keySet().iterator(); iterator.hasNext(); ) {
                Integer key = iterator.next();
                os.write((key + " " + encodingMap.get(key)+ ((iterator.hasNext())?"\n":"")).getBytes());
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private static boolean writeBinaryFile(String inputFile, String outputFileBin, Map<Integer, String> encodingMap) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(inputFile));
        Path outputBinPath = Paths.get(outputFileBin);
        try (DataOutputStream os = new DataOutputStream(Files.newOutputStream(outputBinPath))) {
            StringBuilder sb = new StringBuilder();
            int key;
            while (sc.hasNext()) {
                key = Integer.parseInt(sc.nextLine());
                if(sb.length() >= 32000) {
                    sb = writeBytes(sb, 0, 32000, os);
                }
                sb.append(encodingMap.get(key));
            }
            if(sb.length() != 0) {
                sb = writeBytes(sb, 0, sb.length(), os);
            }
            sc.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private static StringBuilder writeBytes(StringBuilder sb, int start, int end, DataOutputStream os) {
        // not end-start + 1.. because.. we are passign the sb.length.. which is already greater that one..so +1-1.. give end-start
        byte[] arr = new byte[(end-start)/8];
        for(int i = 0, id = 0 ; i < (end-start); i+=8, id++){
            byte temp = 0;
            for(int j = i ; j < Math.min(i+8,end); j++){
                temp+= (sb.charAt(j)-'0') * Math.pow(2, i+8-j-1);
            }
            arr[id] = temp;
        }
        try {
            os.write(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.delete(start, end);
    }
    
    private static Map<Integer, String> getMap(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        Map<Integer, String> encodingMap = new HashMap<>();
        printTree(root, sb, encodingMap);
        return encodingMap;
    }

    private static void printTree(TreeNode root, StringBuilder prefixBuilder, Map<Integer, String> encodingMap) {
        if(root == null) return;
        if(root.left == null && root.right == null) {
//            System.out.format("%d %s\n", ((LeafNode)root).getValue(), prefixBuilder.toString());
            encodingMap.put(((LeafNode)root).getValue(), prefixBuilder.toString());
        }
        prefixBuilder.append("0");
        printTree(root.left, prefixBuilder, encodingMap);
        prefixBuilder.deleteCharAt(prefixBuilder.length()-1);
        prefixBuilder.append("1");
        printTree(root.right, prefixBuilder, encodingMap);
        prefixBuilder.deleteCharAt(prefixBuilder.length()-1);
    }

    private static TreeNode constructHuffmanTree(HashMap<Integer, Integer> fmap, int typeOfHeap) {
//        Heaps<TreeNode> heap = new PriorityQueueHeap<>();
        Heaps<TreeNode> heap = null;
        switch (typeOfHeap){
            case 0: {
                heap = new PairingHeap<>();
                break;
            }
            case 1:{
                heap = new DaryHeap<TreeNode>(2);
                break;
            }
            case 2:{
                heap = new DaryHeap<TreeNode>(4);
                break;
            }
            case 3:{
                heap = new DaryHeap<TreeNode>(4, 3);
                break;
            }
            default: break;
        }
        
        TreeNode temp, left, right;
        for(int key: fmap.keySet()){
            temp = new LeafNode(fmap.get(key), key);
            heap.offer(temp);
        }
        
        while(!heap.isEmpty() && heap.size() != 1){
            left = heap.poll();
            right = heap.poll();
            temp = new TreeNode(left.data + right.data);
            temp.left = left;
            temp.right = right;
            heap.offer(temp);
        }
        return heap.peek();
    }
}
