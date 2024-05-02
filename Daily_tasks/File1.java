package Regex_Part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class File1 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt";
        String[] targetFilePaths = {"/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt", "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_File.txt", "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_file.txt"};

        Set<String> matchedLines = new HashSet<>();

        try {
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
            }

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    boolean matched = false;
                    for (int i = 0; i < targetFilePaths.length; i++) {
                        if (targetLines[i].contains(line)) {
                            System.out.println("Match found in " + targetFilePaths[i] + ": " + line);
                            matchedLines.add(line);
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        System.out.println("Not matched: " + line);
                    }
                }
            }

            System.out.println("\nMatched lines from source file:");
            for (String matchedLine : matchedLines) {
                System.out.println(matchedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
