package Regex_Part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileComparator {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt"; // Change this to your source file path
        String[] targetFilePaths = {"/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt", "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_File.txt", "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_file.txt"}; // Change these to your target file paths

        try {
            // Read lines from target files and store them in sets for fast lookup
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            //declaring array 
            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
                //reading lines from targetFilepath and returns into set of strings.
            }

            // Read lines from source file and compare with lines from target files
            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    boolean matched = false;
                    for (int i = 0; i < targetFilePaths.length; i++) {
                        if (targetLines[i].contains(line)) {
                            System.out.println("Match found in " + targetFilePaths[i] + ": " + line);
                            matched = true;
                            break; // Move to the next line in source file
                        }
                    }
                    if (!matched) {
                        System.out.println("Not matched: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //read each line each file and store in lines
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
