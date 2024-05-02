package REGEX_APRIL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex_Final {
    
    public static boolean match(String input, String pattern) {
        return input.matches(pattern);
    }

    public static void main(String[] args) {
        String srcFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/APRIL_3RD/SRC.txt"; 
        //assigning the source file
        
        String tgtFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/APRIL_3RD/TGT.txt";
        //assigning the target file
        
        try {//THIS IS FOR SOURCE FILE
            StringBuilder srcStringBuilder = new StringBuilder();
            //StringBuilder is the class used build a string, srcStringBuilder is a ref variable 
            //which holds the string from source file
            
            BufferedReader srcBufferedReader = new BufferedReader(new FileReader(srcFilePath));
            // BufferedReader is a class which is used to read a text from line,array, 
            
            String srcLine;//declaring variable
            
            while ((srcLine = srcBufferedReader.readLine()) != null)
            //readaLine() is a method belongs to BufferedReader which reads the line at a time,
            {
                srcStringBuilder.append(srcLine).append("\n");
              //it adds the text contained in srcLine to the end of the StringBuilder.
            }
            
            srcBufferedReader.close();
     
            String srcText = srcStringBuilder.toString();
            // toString() used convert the contents into string format from srcStringBuilder 
            //and assign to srcText
            
           
            //THIS IS FOR TARGET FILE
            StringBuilder tgtStringBuilder = new StringBuilder();
            BufferedReader tgtBufferedReader = new BufferedReader(new FileReader(tgtFilePath));
            String tgtLine;
            while ((tgtLine = tgtBufferedReader.readLine()) != null) {
                tgtStringBuilder.append(tgtLine).append("\n");
            }
            tgtBufferedReader.close();

            String tgtText = tgtStringBuilder.toString();

            Pattern inputPattern = Pattern.compile("\"(.*?)\"");
            
           // System.out.println(inputPattern);
            //Pattern is a class which compile regex
            
            Matcher inputMatcher = inputPattern.matcher(srcText);
            //Matcher is a class which perform match operation,
            //matcher() is a method used to match input to regex
            
            Pattern patternPattern = Pattern.compile("\"(.*?)\"");
            Matcher patternMatcher = patternPattern.matcher(tgtText);
            
           // System.out.println(patternMatcher);
            
            ArrayList<Boolean> trueResults = new ArrayList<>();
            //initialising array list to store boolean values
             
            while (inputMatcher.find() && patternMatcher.find()) 
            //find() is method belongs to matcher class which search for pattern 
            {
                String input = inputMatcher.group(1);
                //group(1) method which capturing first group in input in source file
                
                String pattern = patternMatcher.group(1);
                //group(1) method which capturing first group in pattern in target file
                
                boolean isMatch = match(input, pattern);
                //matching input and pattern
                
                System.out.println("Input: " + input);
                System.out.println("Pattern: " + pattern);
                System.out.println(isMatch);
                
                System.out.println();
                trueResults.add(isMatch);
               
            }
            
            System.out.println(trueResults.toString());
            

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
