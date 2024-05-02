package Regex_Part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class April_25 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_file.txt"
        };

        boolean allMatched = true; 
        Set<String> unmatchedLines = new HashSet<>(); // Store unmatched lines
        
        try {
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
            }

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");

                    boolean matched = false;
                    
                    for (int i = 0; i < targetFilePaths.length; i++) {
                        Set<String> modifiedTargetLines = new HashSet<>();
                        for (String targetLine : targetLines[i]) {
                            modifiedTargetLines.add(targetLine.replaceAll("(?i)\\b(given|then|when|and)\\b", " "));
                        }

                        if (modifiedTargetLines.contains(line)) {
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        allMatched = false;
                        unmatchedLines.add("Line " + lineNumber + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (allMatched) {
            System.out.println("Source file matching with predefined lines");
        } else {
            System.out.println("Some lines in the source file do not match with predefined lines:");
            for (String unmatchedLine : unmatchedLines) {
                System.out.println(unmatchedLine);
            }
        }
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
