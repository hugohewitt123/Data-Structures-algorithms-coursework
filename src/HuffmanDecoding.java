import java.util.*;
import java.io.*;
import java.nio.file.*;
/**
* The class that will decode the file that the user inputs
*/
public class HuffmanDecoding{
    // initialising the arrays and variables
    ArrayList<HuffNode> nodes = new ArrayList<HuffNode>();
    
    /**
    * This class will hold the char an binary from the hufftree file
    */
    public class HuffNode {
        //attributes
        char c;
        String binary;
        
        public HuffNode(char c_/*, String bin_*/){ //to set the attributes of a new node
            //this.binary = bin_;
            this.c = c_;
            
        }
        
        @Override
        public String toString(){ //overriding the tostring to return the frequency and c
            return this.c + "|" + this.binary+"\n";
        }
    }
    
    /**
    *this class does most of the decoding and outputs the files
    */
    public boolean decodeFile() {
        //initialising the scanner
        Scanner input = new Scanner(System.in);
        
        String display = "\nHuffman Decoding\nPlease enter the location of the encoded file to decode\n";
        
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
                    System.out.println("Decoding...");
                    //initialising the variables needed
                    String delimiter = "$";
                    String binarys="";
                    nodes.clear();
                    int j=-1; int c=0; int i=1; int k=0;
                    int y=0;
                    
                    boolean addfile = false;
                    File file = new File(input1 + "/HuffTree.txt");
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {//reading from the file with a buffered reader character by character
                        while((c = br.read()) != -1){ //adding the stored char and binary to the nodes array list as nodes
                            if((char)c == '$' && (char)y != '$'){ //if the char is a $ then end of a char or binary string is reached
                                if(k==1){
                                    nodes.get(j).binary = binarys; //adding the binary string to the node
                                    i=1;
                                } else {
                                    i=0;
                                }
                                binarys="";
                            } else {
                                if (i==1 || ((char)c == '$' && (char)j == '$')){
                                    nodes.add(new HuffNode((char) c)); //adding the char a huffnode object
                                    c=0;
                                    j++;
                                    k=0;
                                } else {
                                    binarys+=Character.toString(c); //adding to the binary string 
                                    k=1;
                                }
                            }
                            y=c;
                        }
                        addfile = true;
                    } catch (Exception e){System.out.println(e);}
                    System.out.println("Tree Decoded");
                    
                    String allBinaryNums = "";
                    // Load bit array from file
                    try {
                        byte[] allBytes = Files.readAllBytes(Paths.get(input1 + "/encoded_file.bin"));
                        allBinaryNums = GetString(allBytes);
                    } catch (IOException ex) {ex.printStackTrace();}
                    
                    sortNodes();
                    System.out.println("Read Encoded file");
                    StringBuilder Builder = new StringBuilder();
                    j=0;
                    for(i=0;i<allBinaryNums.length();i++){
                        for(HuffNode n_ : nodes){
                            if(i-j < n_.binary.length()){
                                break;
                            } else if (allBinaryNums.substring(j,i).equals(n_.binary)){
                                Builder.append(Character.toString(n_.c));
                                j=i;
                            }
                        }
                    }
                    System.out.println("Encoded file decoded using the tree");
                    
                    try {
                        //FileWriter treeWrite = new FileWriter(input1 + "/decodedFile.txt");
                        Writer decodeWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(input1 + "/decodedFile.txt")));
                        decodeWrite.write(Builder.toString());
                        decodeWrite.close();
                    } catch (IOException e) {System.out.println(e);}
                    
                    if (addfile == true){System.out.println("Done! File decoded");}
                } catch (Exception e){System.out.println(e);}
            }
        }
    }
    
    public void sortNodes(){
        HuffNode[] nodesArray = new HuffNode[nodes.size()];
        int j=0;
        for(HuffNode n_ : nodes){
            nodesArray[j] = n_;
            j++;
        }
        boolean sorted = false;
        boolean passes = false;
        while (sorted == false){
            passes = false;
            for (int i=0;i<j-1;i++){
                if (nodesArray[i].binary.length() > nodesArray[i+1].binary.length()){
                    HuffNode temp = nodesArray[i];
                    nodesArray[i]=nodesArray[i+1];
                    nodesArray[i+1]=temp;
                    passes=true;
                }
            }
            if(passes == false){sorted=true;}
        }
        nodes.clear();
        for(HuffNode node_ : nodesArray){
            nodes.add(node_);
        }
    }
    
    static String GetString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }
    public ArrayList<HuffNode> getNodes() {
        return this.nodes;
    }
}
