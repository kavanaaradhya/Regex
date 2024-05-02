package Regex_Part2;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class April_28 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_file.txt"
        };

        try {
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            //This line declares an array named targetLines that can hold sets of strings. 
            //The size of the array is determined by the length of the targetFilePaths array. 
            //Each element of targetLines will hold a Set of strings. 
            
            String[] targetFileNames = new String[targetFilePaths.length];
            //This line declares an array named targetFileNames that can hold strings. 
            //Like targetLines, its size is determined by the length of the targetFilePaths array. 
            //Each element of targetFileNames will hold a file name
            
            boolean[] matchedTargets = new boolean[targetFilePaths.length];
            //This line declares an array named matchedTargets that can hold boolean values. Again, 
            //its size is determined by the length of the targetFilePaths array. 
            //Each element of matchedTargets will indicate whether the corresponding target file has been matched.

            for (int i = 0; i < targetFilePaths.length; i++) {
            	//This is a for loop that iterates over each element of the targetFilePaths array.
            	
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
               // Inside the loop, it calls a method readLinesFromFile() with the current targetFilePaths[i] as an argument. 
                //This method reads lines from the file specified by targetFilePaths[i] and returns a set of strings representing the lines. 
                //The set of strings is then assigned to the i-th element of the targetLines array.
                
                targetFileNames[i] = getFileName(targetFilePaths[i]);
                // This line calls a method getFileName() with the current targetFilePaths[i] as an argument. 
                //This method extracts the file name from the path specified by targetFilePaths[i] and assigns it to the i-th element of the targetFileNames array.
            }

            FileWriter writer = new FileWriter("/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/testout.html");
            //It specifies the path where the file will be created and the file name. In this case, 
            //it's creating a file named "testout.html" in the specified directory.
            //If the file already exists, its contents will be overwritten. If the file does not exist, a new file will be created.
            
            BufferedWriter buffer = new BufferedWriter(writer);

            buffer.write("<html><!DOCTYPE html>\n"
                    + "<html lang=\"en\">\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "<title>Table with Three Columns</title>\n"
                    + "<style>\n"
                    + "    /* Some basic styling for demonstration */\n"
                    + "    table {\n"
                    + "        width: 100%;\n"
                    + "        border-collapse: collapse;\n"
                    + "    }\n"
                    + "    th, td {\n"
                    + "        border: 1px solid black;\n"
                    + "        padding: 8px;\n"
                    + "        text-align: center;\n"
                    + "    }\n"
                    + "    th {\n"
                    + "        background-color: #f2f2f2;\n"
                    + "    }\n"
                    + "</style>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "\n"
                    + "<h2>Comparison of contents</h2>\n"
                    + "\n"
                    + "<table>\n"
                    + "    <tr>\n"
                    + "        <th>Source Step</th>\n"
                    + "        <th>Predefined Step</th>\n"
                    + "        <th>Status</th>\n"
                    + "    </tr>\n");

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath)))
            {//This line starts a try-with-resources block. It creates a BufferedReader named br that reads text from a character-input stream, 
            	//which in turn is initialized with a FileReader that reads from the file specified by sourceFilePath. 
            	//The try-with-resources statement ensures that the BufferedReader is closed properly after the block is executed, 
            	//even if an exception occurs.
            	
                String line;
                //Declares a String variable named line to store each line of text read from the file.
                
                while ((line = br.readLine()) != null) {
                	//This is a while loop that reads each line from the file using the readLine() method of the BufferedReader. 
                	//It continues looping until readLine() returns null, indicating the end of the file.
                    
                	line = line.trim(); 
                	//Trims leading and trailing whitespace characters from the line using the trim() method. 
                	//This removes any leading spaces, tabs, or trailing spaces from the line.
                	
                    if (!line.isEmpty()) { 
                    	//Checks if the trimmed line is not empty after removing whitespace characters.
                    	
                        line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                        //This line replaces all occurrences of the words "given", "then", "when", or "and" (case-insensitive) with a space. 
                        //The (?i) at the beginning of the regular expression makes the pattern case-insensitive, and \b represents a word boundary. 
                        //This effectively removes these words from the line.
                        
                        boolean matched = false;
                        //Declares a boolean variable named matched and initializes it to false. 
                        //This variable seems to be intended for some purpose within the loop, 
                        //but its purpose is not clear from the provided code snippet.

                        for (int i = 0; i < targetFilePaths.length; i++) {
                        //This loop iterates over each target file path.
                        	
                            for (String targetLine : targetLines[i]) {
                            	//This is an enhanced for loop that iterates over each line in the set of strings (targetLines[i]) 
                            	//corresponding to the current target file.
                            	
                                if (targetLine.replaceAll("(?i)\\b(given|then|when|and)\\b", " ").equals(line)) {
                                	//This is an enhanced for loop that iterates over each line in the set of strings (targetLines[i]) 
                                	//corresponding to the current target file.
                                	
                                    buffer.write("<tr><td>" + line + "</td><td>" + targetLine + "</td><td>Matched</td></tr>\n");
                                    //If a match is found, it writes an HTML table row (<tr>) with three cells (<td>) containing the line, 
                                    //the targetLine, and the status "Matched".
                                    
                                    matchedTargets[i] = true;
                                    //Marks the i-th target file as matched
                                    
                                    matched = true;
                                    //Sets the matched variable to true, indicating that a match is found.
                                    
                                    break;
                                    //Breaks out of the inner loop once a match is found.
                                }
                            }
                            if (matched) {
                                break;
                                //Breaks out of the outer loop if a match is found.
                            }
                        }
                        if (!matched) {
                        	//This block of code executes if no match is found for the current line in any of the target files. 
                        	//It writes an HTML table row indicating "Not Matched".
                        	
                            buffer.write("<tr><td>" + line + "</td><td></td><td>Not Matched</td></tr>\n");
                            //Writes an HTML table row indicating that the current line 
                            //from the source file did not match any lines in the target files.
                            
                            for (int i = 0; i < targetFilePaths.length; i++) {
                            	//This loop iterates over each target file and checks if it was not matched. However, 
                            	//the block inside the loop is empty, so there's no code executed for unmatched target files.
                                if (!matchedTargets[i]) {
                                    
                                }
                            }
                        }
                        for (int i = 0; i < matchedTargets.length; i++) {
                            matchedTargets[i] = false;
                            //Resets all elements of the matchedTargets array to false after processing each line from the source file. 
                            //This prepares the matchedTargets array for the next iteration, 
                            //ensuring that each target file's match status is cleared.
                        }
                    }
                }
            }
            buffer.write("</table>\n"
                    + "\n"
                    + "</body>\n"
                    + "</html>\n"
                    + "</html>");

            buffer.close();
            System.out.println("HTML file has been generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    private static Set<String> readLinesFromFile(String filePath) throws IOException {
        Set<String> lines = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Trim leading and trailing spaces
                if (!line.isEmpty()) { // Check if line is not empty after trimming
                    lines.add(line.replaceAll("(?i)\\b(given|then|when|and)\\b", " "));
                }
            }
        }
        return lines;
        /*This method reads lines from a file specified by the filePath.
It initializes an empty HashSet named lines to store the lines read from the file.
It opens a BufferedReader to read from the file specified by filePath.
Inside a try-with-resources block, it reads each line from the file using br.readLine().
For each line read, it trims leading and trailing spaces using line.trim(), and if the trimmed line is not empty, it replaces occurrences of "given", "then", "when", or "and" with a space, and adds the resulting line to the lines set.
Once all lines are read from the file, it returns the lines set containing the unique lines read from the file.
   */ }
}
