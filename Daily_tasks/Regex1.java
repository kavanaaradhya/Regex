package Daily_tasks;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex1 {
    private static final String HTML_FILE_PATH = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/testout_regex.html";
    private static final String[] TARGET_FILE_PATHS = {
            "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/TGT_GIVEN_REGEX.txt",
            "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/TGT_THEN_REGEX.txt",
            "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/TGT_WHEN_REGEX.txt"
    };

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the path to the source file:");
        String sourceFilePath = scanner.nextLine();
        System.out.println("\u001B[31m::::::: :::::::: G E N E R A T I N G - R E P O R T :::::: :::::::::\u001B[0m");
        Thread.sleep(2000);

        generateReport(sourceFilePath);
    }

    private static void generateReport(String sourceFilePath) {
        try {
            Set<String>[] targetLines = new HashSet[TARGET_FILE_PATHS.length];

            for (int i = 0; i < TARGET_FILE_PATHS.length; i++) {
                targetLines[i] = readLinesFromFile(TARGET_FILE_PATHS[i]);
            }

            FileWriter writer = new FileWriter(HTML_FILE_PATH);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(generateHtmlHeader());

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                        boolean matched = false;

                        for (int i = 0; i < TARGET_FILE_PATHS.length; i++) {
                            for (String targetLine : targetLines[i]) {
                                targetLine = targetLine.replaceAll("\\b(Given|When|Then)\\b", " ");
                                Pattern pattern = Pattern.compile(targetLine);
                                Matcher matcher = pattern.matcher(line);
                                if (matcher.matches()) {
                                    buffer.write("<tr class=\"matched\"><td>" + line + "</td><td>" + targetLine + "</td><td>Matched</td></tr>\n");
                                    matched = true;
                                    break;
                                }
                            }
                            if (matched) break;
                        }
                        if (!matched) {
                            buffer.write("<tr class=\"not-matched\"><td>" + line + "</td><td>The given source step is not matched with any of predefined steps</td><td>Not Matched</td></tr>\n");
                        }
                    }
                }
            }
            buffer.write(generateHtmlFooter());
            buffer.close();

            System.out.println("\u001B[32m:::::: ::::::::: REPORT GENERATED SUCCESSFULLY ::::::: ::::::::::::\u001B[0m");
            System.out.println("To view the HTML report, click on the following link:");
            System.out.println(HTML_FILE_PATH);
            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private static String generateHtmlHeader() {
        return "<html><!DOCTYPE html>\n"
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
                + "    .matched { background-color: lightgreen; }\n"
                + "    .not-matched { background-color: lightcoral; }\n"
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
                + "    </tr>\n";
    }

    private static String generateHtmlFooter() {
        return "</table>\n"
                + "</body>\n"
                + "</html>\n";
    }

    private static Set<String> readLinesFromFile(String filePath) throws IOException {
        Set<String> lines = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line.replaceAll("(?i)\\b(given|then|when|and)\\b", " "));
                }
            }
        }
        return lines;
    }
}
