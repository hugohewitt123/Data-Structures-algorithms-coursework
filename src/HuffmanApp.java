import java.util.*;
import java.io.*;
/**
* The class that will call the huffman encoding and decoding classes according to user inputs
*/
public class HuffmanApp{
    public static void main(String[] args) {
        clrscr();
        //initialising the scanner
        Scanner input = new Scanner(System.in);
        
        HuffmanEncoding huffmanEncoding = new HuffmanEncoding();
        
        HuffmanDecoding huffmanDecoding = new HuffmanDecoding();
        
        //showing all the options the user has to input for encoding and decoding
        String displayScreen = "\nHuffman Encoding and Decoding app \n\nPlease, enter the number to select your option:\n\nTo Encode or Decode:\n\t1. Encode a file\n\t2. Decode a file\n\nAfter selecting one the options above, you will be presented other screens.\nIf you press 0, you will be able to return to this main menu.\nPress -1 (or ctrl+c) to quit this application.\n";
        
        boolean exit = true;
        
        while (exit == true){
            System.out.println(displayScreen);
            //waiting for the user to select an option
            String input1 = input.nextLine();
            //if they select -1 exit the application
            if (input1.equals("-1")){
                exit = false;
            } else if (input1.equals("1")){ //if they select 1, go to the encoding class
                clrscr();
                exit = huffmanEncoding.encodeFile();
                clrscr();
                //System.out.println(huffmanEncoding.getNodes());
            } else if (input1.equals("2")){ //if they select 2, go to the decoding class
                clrscr();
                exit = huffmanDecoding.decodeFile();
                clrscr();
                //System.out.println(huffmanDecoding.getNodes());
            } else {
                clrscr();
                exit = true;
                clrscr();
            }
        }
    }
    /**
    *this class clears the terminal
    */
    public static void clrscr(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {}
    }
}
