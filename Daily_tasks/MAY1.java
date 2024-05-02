package Regex_Part2;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MAY1 {
    public static void main(String[] args) {
        String sourceFilePath = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/SRC_REGEX.txt";
        String[] targetFilePaths = {
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/TGT_GIVEN_REGEX.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/TGT_THEN_REGEX.txt",
                "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Regex_Part2/TGT_WHEN_REGEX.txt"
        };

        try {
            Set<String>[] targetLines = new HashSet[targetFilePaths.length];
            String[] targetFileNames = {"TGT_GIVEN_REGEX.txt", "TGT_THEN_REGEX.txt", "TGT_WHEN_REGEX.txt"};
            boolean[] matchedTargets = new boolean[targetFilePaths.length];

            for (int i = 0; i < targetFilePaths.length; i++) {
                targetLines[i] = readLinesFromFile(targetFilePaths[i]);
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
                        String firstWord = line.split("\\s+")[0].toLowerCase(); // Get the first word and convert to lowercase
                        int targetIndex = -1;

                        if (firstWord.equals("given")) {
                            targetIndex = 0;
                        } else if (firstWord.equals("then")) {
                            targetIndex = 1;
                        } else if (firstWord.equals("when")) {
                            targetIndex = 2;
                        }

                        if (targetIndex != -1) {
                            boolean matched = false;

                            for (String targetLine : targetLines[targetIndex]) {
                                Pattern pattern = Pattern.compile(targetLine);
                                Matcher matcher = pattern.matcher(line);
                                if (matcher.matches()) {
                                    buffer.write("<tr class=\"matched\"><td>" + line + "</td><td>" + targetLine + "</td><td>Matched</td></tr>\n");
                                    matchedTargets[targetIndex] = true;
                                    matched = true;
                                    break;
                                }
                            }

                            if (!matched) {
                                buffer.write("<tr class=\"not-matched\"><td>" + line + "</td><td></td><td>Not Matched</td></tr>\n");
                                if (!matchedTargets[targetIndex]) {
                                    // Do something if not matched
                                }
                            }

                            matchedTargets[targetIndex] = false;
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
