package Regex_Part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class April_19 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_file.txt"
        };

        boolean allMatched = true; // This line declares a boolean variable allMatched and initializes it to true. 
        //This variable will be used to track whether all lines from the source file are matched.

        try {
            // Read lines from target files and store them in sets for fast lookup
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            //This line declares an array of sets (targetLines) to store the lines from the target files.
            for (int i = 0; i < targetFilePaths.length; i++)
            	//This line starts a loop that iterates through the paths of the target files.
            {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
                //This line calls the readLinesFromFile method to read the lines from the target file 
                //at index i and stores them in the targetLines array.
            }

            // Read lines from source file and compare with lines from target files
            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null)
                //This line starts a loop that reads each line from the source file until the end of the file is reached
                	{
                    lineNumber++;
                    // Replace specific keywords with spaces
                    line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");

                    boolean matched = false;
                    // This variable will be used to track whether the current line from the source file 
                    //matches any line from the target files.
                    
                    
                    for (int i = 0; i < targetFilePaths.length; i++) {
                        // Replace specific keywords with spaces in target lines
                        Set<String> modifiedTargetLines = new HashSet<>();
                        for (String targetLine : targetLines[i])
                        //This line starts a loop that iterates through the lines of the current target file.
                        	{
                            modifiedTargetLines.add(targetLine.replaceAll("(?i)\\b(given|then|when|and)\\b", " "));
                        }

                        if (modifiedTargetLines.contains(line)) {
                            matched = true;
                            break; // Move to the next line in source file
                        }//This block checks if the modified line from the source file exists in the modifiedTargetLines set. 
                        //If it does, it sets the matched flag to true and breaks out of the loop, 
                        //as there is no need to continue checking the other target files.
                    }
                    if (!matched) {
                        allMatched = false;
                        System.out.println("The given line in the source step is not matching with predefined step with line num: " + lineNumber + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print the final result based on allMatched flag
        if (allMatched) {
            System.out.println("Source file matching with predefined lines");
        } //This block is executed after processing all lines from the source file. If the allMatched flag is still true, 
        //it prints a message indicating that the source file matches all predefined lines.
        
    }

    private static Set<String> readLinesFromFile(String filePath) throws IOException {
        Set<String> lines = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
