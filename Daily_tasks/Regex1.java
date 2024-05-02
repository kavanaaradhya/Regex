package Regex_Part2;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex1 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/SRC_REGEX.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/TGT_GIVEN_REGEX.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/TGT_THEN_REGEX.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/TGT_WHEN_REGEX.txt"
        };

        try {
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            String[] targetFileNames = new String[targetFilePaths.length];
            boolean[] matchedTargets = new boolean[targetFilePaths.length];

            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
                targetFileNames[i] = getFileName(targetFilePaths[i]);
            }

            FileWriter writer = new FileWriter("/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/testout_regex.html");
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
                    + "    .matched { background-color: lightgreen; }\n" // Style for matched rows
                    + "    .not-matched { background-color: lightcoral; }\n" // Style for not matched rows
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

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim(); // Trim leading and trailing spaces
                    if (!line.isEmpty()) { // Check if line is not empty after trimming
                        // Replace specified words with spaces
                    	line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");// Replace specified words in uppercase with spaces
                        boolean matched = false;

                        for (int i = 0; i < targetFilePaths.length; i++) {
                            for (String targetLine : targetLines[i]) {
                                //targetLine = targetLine.replaceAll("\\b(given|when|then)\\b", " "); // Replace specified words with spaces
                                targetLine = targetLine.replaceAll("\\b(Given|When|Then)\\b", " "); // Replace specified words in uppercase with spaces
                                Pattern pattern = Pattern.compile(targetLine);
                                Matcher matcher = pattern.matcher(line);
                                if (matcher.matches()) {
                                    buffer.write("<tr class=\"matched\"><td>" + line + "</td><td>" + targetLine + "</td><td>Matched</td></tr>\n");
                                    matchedTargets[i] = true;
                                    matched = true;
                                    break;
                                }
                            }
                            if (matched) {
                                break;
                            }
                        }
                        if (!matched) {
                            buffer.write("<tr class=\"not-matched\"><td>" + line + "</td><td></td><td>Not Matched</td></tr>\n");
                            for (int i = 0; i < targetFilePaths.length; i++) {
                                if (!matchedTargets[i]) {
                                    // Do something if not matched
                                }
                            }
                        }
                        for (int i = 0; i < matchedTargets.length; i++) {
                            matchedTargets[i] = false;
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
    }
}
