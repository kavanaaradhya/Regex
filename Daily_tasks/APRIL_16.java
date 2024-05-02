package Regex_Part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class APRIL_16 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_file.txt"
        };
        Set<String> matchedLines = new HashSet<>();


        try {
            // Read lines from target files and store them in sets
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            String[] targetFileNames = new String[targetFilePaths.length];
            boolean[] matchedTargets = new boolean[targetFilePaths.length]; // To track which targets are matched
            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
                targetFileNames[i] = getFileName(targetFilePaths[i]);
            }

            // Process the source file
            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Replace specific keywords with spaces
                    line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                    boolean matched = false;
                    // Compare the modified line with lines from each target file
                    for (int i = 0; i < targetFilePaths.length; i++) {
                        for (String targetLine : targetLines[i]) {
                            if (targetLine.replaceAll("(?i)\\b(given|then|when|and)\\b", " ").equals(line)) {
                                 System.out.println("Match found in " + targetFileNames[i] + ": " + line);
                                matchedLines.add(line);
                                matchedTargets[i] = true; // Mark this target as matched
                                matched = true;
                                break;
                            }
                        }
                        if (matched) {
                            break;
                        }
                    }
                    // If no match is found, print the line and mismatched target file name
                    if (!matched) {
                        System.out.println("Not matched: " + line);
                        for (int i = 0; i < targetFilePaths.length; i++) {
                            if (!matchedTargets[i]) {
                                System.out.println("Mismatched in file: " + targetFileNames[i]);
                            }
                        }
                    }
                    // Reset matchedTargets array for the next line
                    for (int i = 0; i < matchedTargets.length; i++) {
                        matchedTargets[i] = false;
                    }
                }
            }
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
                lines.add(line.replaceAll("(?i)\\b(given|then|when|and)\\b", " "));
            }
        }
        return lines;
    }
}
