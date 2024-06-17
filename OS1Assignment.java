//Azhar Abdool
//OS1 Assignment 1
//

import java.io.*;
import java.util.*;

public class OS1Assignment {
    public static void main(String[] args) {
        if (args.length != 1) {         //check args len to make sure
            System.out.println("Please provide the input sequence filename as a command-line argument.");
            return;
        }

        
        String inputFilename = args[0]; //getting the input file name from args
        String outputFilename = "output-OS1";   //output file name

        try (
            
            FileInputStream input = new FileInputStream(inputFilename); //open stream to read from file
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilename);   //open output stream to write to new file
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);//to be able to write to file
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            int[] pageTable = {2, 4, 1, 7, 3, 5, 6}; //page Table to Physical Frame mapping as per fig

            byte[] dataBuffer = new byte[8];    //storing bytes from input

            
            while (input.read(dataBuffer) != -1) {      //read whole file
                
                long virtualAddress = byteArrayToLong(dataBuffer);  //convert to virtual long addy
                int pageNumber = (int) (virtualAddress >> 7); //page number, 7 bits
                int pageOffset = (int) (virtualAddress & 0x7F); //page offset, 7 bits

                int frameNumber = pageTable[pageNumber];    //get frame no. through map from page no.

                long physicalAddress = (frameNumber << 7) + pageOffset;     //calc physical addy with frame and oofset




                String physicalAddressStr = "0x" + Long.toHexString(physicalAddress);       //as hex

                System.out.println(physicalAddressStr); //print address

                bufferedWriter.write(physicalAddressStr);   //write to file
                bufferedWriter.newLine();//leave line
            }
        } catch (IOException e) { //error handle
            e.printStackTrace();
        }
    }

    private static long byteArrayToLong(byte[] bytes) { //method for converting byte to long
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result |= (bytes[i] & 0xFFL) << (8 * i);
        }
        return result;
    }
}
