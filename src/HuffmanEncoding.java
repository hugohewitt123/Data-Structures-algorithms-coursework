import java.util.*;
import java.io.*;
import java.nio.file.*;
/**
* The class that will encode the text file the user specifies
*/
public class HuffmanEncoding{
    // initialising the arrays and variables
    File file;
    int c = 0;
    ArrayList<Character> chars = new ArrayList<Character>();
    ArrayList<HuffNode> nodes = new ArrayList<HuffNode>();
    
    /**
    *class to implement comparitor
    */
    class Compare implements Comparator<HuffNode> {
        public int compare(HuffNode x, HuffNode y){
            return x.frequency - y.frequency;
        }
    }
    
    /**
    * This class will hold the nodes of the tree
    */
    public class HuffNode {
        //attributes
        int frequency;
        char c;
        HuffNode left;
        HuffNode right;
        int leftIndex;
        int rightIndex;
        String binary;
        
        public HuffNode(int f_, char c_){ //to set the attributes of a new node
            this.frequency = f_;
            this.c = c_;
            //this.code = "-1";
            this.left = null;
            this.right = null;
        }
        
        public HuffNode(int f_){ //to set the attributes of a new node
            this.frequency = f_;
            //this.code = "-1";
            this.left = null;
            this.right = null;
        }
        
        public void setNodes(HuffNode left_, HuffNode right_){ //to set left and right
            this.left = left_;
            this.right = right_;
        }
        
        public void setBin(String bin_){ //to set the binary of the node
            this.binary = bin_;
        }
        
        public String getBin(){ //to set the code of the node
            return this.binary;
        }
        
        public char getChar(){ //return the char in that node
            return this.c;
        }
        
        public void increment(){ // incrementing the frequency ina node
            this.frequency++;
        }
        
        public String toFileFormat(){ //returning the contents of the node in a format to store in the file
            return this.c + "$" + this.binary + "$";
        }
        
        @Override
        public String toString(){ //overriding the tostring to return the frequency and c
            return this.c + "|" + this.frequency + "|" + this.left+ "|" + this.right + "|" + this.binary+"\n";
        }
    }
    
    /**
    *the class that does most of the encoding
    */
    public boolean encodeFile() {
        //initialising the scanner
        Scanner input = new Scanner(System.in);
        
        String display = "\nHuffman Encoding\nPlease enter the location and name of the text file you wish to encode\n";
        
        display = display + "\n\n0. Back to main menu.\n-1. Quit application.\n\n";
        System.out.println(display);
        
        while (true){
            String input1 = input.nextLine();
            //if they select -1 exit the application
            if (input1.equals("-1")){
                return false;
            } else if (input1.equals("0")){ //if they select 1 go to the bookable rooms list
                return true;
            } else {
                try{
                    System.out.println("Encoding...");
                    //reseting variables
                    chars.clear();
                    nodes.clear();
                    c=0;
                
                    boolean addfile = false;
                    file = new File(input1);
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {//readign from the file with a buffered reader character by character
                        while((c = br.read()) != -1){
                            chars.add((char) c); // add the characters to the array list chars
                        }
                        addfile = true;
                    } catch (Exception e){System.out.println(e);}
                    System.out.println("File Read");
                    boolean duplicate = false; //initialising variables to be used when creating the nodes for the tree
                    int i = 0;
                    HuffNode temp;
                    for (char c_ : chars){ //looping through each caracter
                        for (HuffNode n_ : nodes){ //looping through each already added node
                            if (Character.toString(c_).equals(Character.toString(n_.getChar()))) {//checking to see if there is a node with that character already stored
                                duplicate = true; 
                                break;
                            }
                            i++;
                        }
                        if (duplicate == true){ //if there is a node with that char already stored then increment its freuency
                            nodes.get(i).increment();
                        } else { //if not already stored, create a new one with frequency 1
                            nodes.add(new HuffNode(1, c_));
                        }
                        duplicate = false;//re-setting the variables
                        i=0;
                    }
                    System.out.println("Nodes created");
                    PriorityQueue<HuffNode> HuffQueue = new PriorityQueue<HuffNode>(nodes.size(), new Compare());
                
                    // create a root node
                    HuffNode root = null;
                
                    for (HuffNode n_ : nodes){
                        HuffQueue.add(n_);
                    }
    
                    while (HuffQueue.size() > 1) {
                        // first min extract.
                        HuffNode x = HuffQueue.peek();
                        HuffQueue.poll();
    
                        // second min extarct.
                        HuffNode y = HuffQueue.peek();
                        HuffQueue.poll();
 
                        // new node p which is equal to the node frequencies added together
                        HuffNode p = new HuffNode(x.frequency + y.frequency,'$');
 
                        p.left = x;// first extracted node as left child.
                        p.right = y; // second extracted node as the right child.
                        root = p; // marking the f node as the root node.
 
                        HuffQueue.add(p); // add this node to the priority-queue.
                    }
                    assignCodes(root,"");
                    System.out.println("Tree Created");
                
                    File f1 = new File(input1.substring(0,input1.length()-3));
                    //Creating a new folder with the user inputed location 
                    f1.mkdirs();
                
                    String fileOutput = "";
                    int j = 0;
                    while (j<nodes.size()){
                        fileOutput += nodes.get(j).toFileFormat();
                        j++;
                    }
                
                    try {
                        Writer treeWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(input1.substring(0,input1.length()-3) + "/HuffTree.txt")));
                        treeWrite.write(fileOutput);
                        treeWrite.close();
                    } catch (IOException e) {System.out.println(e);}
                    System.out.println("Tree File Created");
                    ///////////////////////////////////////////////////////////////////////////////////////////////
                
                    StringBuilder Builder = new StringBuilder();
                    int z=0;
                    for (char char_ : chars){
                        for (HuffNode node_ : nodes){ //looping through each already added node
                            if (Character.toString(char_).equals(Character.toString(node_.getChar()))) {
                                Builder.append(node_.getBin());
                                break;
                            }
                        }
                    }
                    
                    byte[] converted = GetBinary(Builder.toString());
                    System.out.println("Chars converted to binary bits");
                    // Save bit array to file
                    try {
                        OutputStream outputStream = new FileOutputStream(input1.substring(0,input1.length()-3) + "/encoded_file.bin");
                        outputStream.write(converted);
                        outputStream.close();
                    } catch (IOException e) {e.printStackTrace();}
                    
                    if (addfile == true){System.out.println("Done! File encoded");}
                } catch (Exception e){System.out.println(e);}
            }
        }
    }
    
    /**
    *this class converts a binary string to binary bits
    */
    static byte[] GetBinary(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        while (stringBuilder.length() % 8 != 0) {
            stringBuilder.append('0');
        }
        s = stringBuilder.toString();

        byte[] data = new byte[s.length() / 8];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            }
        }
        return data;
    }
    
    /**
    *this class works out the binary codes of each leaf
    */
    public void assignCodes(HuffNode root, String s){
        // base case; if the left and right are null then its a leaf node then assign the binary code s
        if (root.left == null && root.right == null) {
            root.setBin(s);
            return;
        }
        // To left then add "0" to the code. To the right add "1" to the code.
        assignCodes(root.left, s+"0");// recursive calls for left and right sub-tree of the tree.
        assignCodes(root.right, s+"1");
    }
    /**
    *returns the array list of chars
    */
    public ArrayList<Character> getFile() {
        return this.chars;
    }
    
    /**
    *returns the array list of nodes
    */
    public ArrayList<HuffNode> getNodes() {
        return this.nodes;
    }
}
