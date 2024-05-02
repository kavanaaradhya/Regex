package Regex_Part2;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class April_30 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Source_file.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Given_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_When_File.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/Target_Then_file.txt"
        };

        try {
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            String[] targetFileNames = new String[targetFilePaths.length];
            boolean[] matchedTargets = new boolean[targetFilePaths.length];

            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
                targetFileNames[i] = getFileName(targetFilePaths[i]);
            }

            FileWriter writer = new FileWriter("/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/testout.html");
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
                        line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                        boolean matched = false;

                        for (int i = 0; i < targetFilePaths.length; i++) {
                            for (String targetLine : targetLines[i]) {
                                if (targetLine.replaceAll("(?i)\\b(given|then|when|and)\\b", " ").equals(line)) {
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
                            buffer.write("<tr class=\"not-matched\"><td>" + line + "</td>");
                            for (int i = 0; i < targetFilePaths.length; i++) {
                                if (!matchedTargets[i]) {
                                    // Get the contents from the target file
                                    StringBuilder targetContent = new StringBuilder();
                                    for (String targetLine : targetLines[i]) {
                                        targetContent.append(targetLine).append("<br>");
                                    }
                                    // Write the contents of the target file in the second column
                                    buffer.write("<td>" + targetContent.toString() + "</td>");
                                }
                            }
                            buffer.write("<td>Not Matched</td></tr>\n");
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
