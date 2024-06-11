package Daily_tasks;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SUGGESTION_7 {
    private static final String HTML_FILE_PATH = "/Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/New_suggestion.html";
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

            int totalNumberMatched = 0;
            int totalNumberFailed = 0;

            FileWriter writer = new FileWriter(HTML_FILE_PATH);
            BufferedWriter buffer = new BufferedWriter(writer);
            int[] counts = countMatchedAndNotMatched(sourceFilePath, targetLines);
            totalNumberMatched = counts[0];
            totalNumberFailed = counts[1];
            buffer.write(generateHtmlHeader(totalNumberMatched, totalNumberFailed));

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
                String line;
                boolean insideScenario = false;
                StringBuilder scenarioBuilder = new StringBuilder();
                boolean scenarioHasFailedSteps = false;

                while ((line = br.readLine()) != null) {
                    line = line.trim().replaceAll("\\s+", " "); // Normalize spacing between words
                    if (!line.isEmpty()) {
                        if (line.startsWith("Scenario")) {
                            if (insideScenario) {
                                writeScenario(buffer, scenarioBuilder.toString(), scenarioHasFailedSteps);
                            }
                            insideScenario = true;
                            scenarioBuilder = new StringBuilder();
                            scenarioHasFailedSteps = false;
                            scenarioBuilder.append("<tr class=\"scenario\">"
                                    + "<td colspan=\"4\">" // Changed colspan to 4
                                    + "<details>"
                                    + "<summary style=\"text-align: left;\">" + line + "</summary>"
                                    + "<div style=\"margin-left: 0px;\">"
                                    + "<table style=\"width: 100%; border-collapse: collapse;\">"
                                    + "<tr>"
                                    + "<th style=\"border: 2px solid white; padding: 8px; text-align: center;\">SOURCE STEP</th>"
                                    + "<th style=\"border: 2px solid white; padding: 8px; text-align: center;\">PREDEFINED STEP</th>"
                                    + "<th style=\"border: 2px solid white; padding: 8px; text-align: center;\">STATUS</th>"
                                    + "<th style=\"border: 2px solid white; padding: 8px; text-align: center;\">SUGGESTION</th>" // Added Suggestion column
                                    + "</tr>");
                        } else {
                            line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                            boolean matched = false;
                            String matchedTargetLine = "";
                            for (int i = 0; i < TARGET_FILE_PATHS.length; i++) {
                                for (String targetLine : targetLines[i]) {
                                    targetLine = targetLine.replaceAll("\\b(Given|When|Then|And)\\b", " ");
                                    Pattern pattern = Pattern.compile(targetLine);
                                    Matcher matcher = pattern.matcher(line);
                                    if (matcher.matches()) {
                                        scenarioBuilder.append("<tr class=\"matched\">"
                                                + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">" + line + "</td>"
                                                + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">" + targetLine + "</td>"
                                                + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">Matched</td>"
                                                + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">No suggestion available</td>" // No suggestion for matched
                                                + "</tr>\n");
                                        matched = true;
                                        matchedTargetLine = targetLine;
                                        totalNumberMatched++;
                                        break;
                                    }
                                }
                                if (matched) {
                                    break;
                                }
                            }
                            if (!matched) {
                                String suggestion = generateSuggestion(line, targetLines);
                                scenarioBuilder.append("<tr class=\"not-matched\">"
                                        + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">" + line + "</td>"
                                        + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">The given source step is not matched with any of predefined steps</td>"
                                        + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">Not Matched</td>"
                                        + "<td style=\"border: 2px solid white; padding: 8px; text-align: left;\">" + suggestion + "</td>" // Added suggestion for not matched
                                        + "</tr>\n");
                                totalNumberFailed++;
                                scenarioHasFailedSteps = true;
                            }
                        }
                    }
                }
                if (insideScenario) {
                    writeScenario(buffer, scenarioBuilder.toString(), scenarioHasFailedSteps);
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

    private static String generateHtmlHeader(int totalNumberMatched, int totalNumberFailed) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>HTML REPORT</title>\n"
                + "    <style>\n"
                + "        /* Some basic styling for demonstration */\n"
                + "        table {\n"
                + "            width: 100%;\n"
                + "            border-collapse: collapse;\n"
                + "        }\n"
                
                + "        th,\n"
                + "        td {\n"
                + "            border: 2px solid white; /* Changed border width to 2px */\n"
                + "            padding: 8px;\n"
                + "            text-align: center;\n"
                + "        }\n"
                
                + "        th {\n"
                + "            background-color: #fff3cd; /* Light yellow background color */\n"
                + "            font-weight: bold; /* Making table headers bold */\n"
                + "            font-size: 20px; /* Increase font size for table headers */\n"
                + "        }\n"
                
                + "        .matched {\n"
                + "            background-color: #8FBC8F; /* Light green color for matched */\n"
                + "        }\n"
                
                + "        .not-matched {\n"
                + "            background-color: #f69697; /* Light red color for not matched */\n"
                + "        }\n"
                
                + "        .scenario.matched {\n"
                + "            background-color: #bff2bf; /* Light green color for matched scenario */\n"
                + "        }\n"
                + "        .scenario.not-matched {\n"
                + "            background-color: #FFCCCB; /* Light red color for not matched scenario */\n"
                + "        }\n"
                + "        .container {\n"
                + "            width: 100%; /* Adjust the width as needed */\n"
                + "            padding: 0px;\n"
                + "            background-color: #cce5ff; /* Background color for the container */\n"
                + "            color: black; /* Text color for the container */\n"
                + "            margin: auto; /* Center the container */\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"container\">\n"
                + "        <h2 id=\"comparison-header\" style=\"text-align: center;\">AUTOMATION STEPS COMPARISON TOOL</h2>\n"
                + "        <p>Time Stamp: " + formattedTime + "</p>\n"
                + "        <p style=\"color: #32CD32;\">Total Number Matched: " + totalNumberMatched + "</p>\n"
                + "        <p style=\"color: #dc3545;\">Total Number Not Matched: " + totalNumberFailed + "</p>\n"
                + "    </div>\n"
                + "    <table>\n"
                + "        <tr>\n"
                + "            <th>TESTCASE SCENARIOS</th>\n"
                + "        </tr>\n";
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
                line = line.trim().replaceAll("\\s+", " "); // Normalize spacing between words
                if (!line.isEmpty()) {
                    lines.add(line.replaceAll("(?i)\\b(given|then|when|and)\\b", " "));
                }
            }
        }
        return lines;
    }

    private static int[] countMatchedAndNotMatched(String sourceFilePath, Set<String>[] targetLines) throws IOException {
        int totalNumberMatched = 0;
        int totalNumberFailed = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().replaceAll("\\s+", " "); // Normalize spacing between words
                if (!line.isEmpty()) {
                    if (!line.startsWith("Scenario")) {
                        line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                        boolean matched = false;

                        for (int i = 0; i < TARGET_FILE_PATHS.length; i++) {
                            for (String targetLine : targetLines[i]) {
                                targetLine = targetLine.replaceAll("\\b(Given|When|Then|And)\\b", " ");
                                Pattern pattern = Pattern.compile(targetLine);
                                Matcher matcher = pattern.matcher(line);
                                if (matcher.matches()) {
                                    matched = true;
                                    break;
                                }
                            }
                            if (matched) {
                                totalNumberMatched++;
                                break;
                            }
                        }
                        if (!matched) {
                            totalNumberFailed++;
                        }
                    }
                }
            }
        }
        return new int[]{totalNumberMatched, totalNumberFailed};
    }

    private static void writeScenario(BufferedWriter buffer, String scenarioContent, boolean hasFailedSteps) throws IOException {
        if (hasFailedSteps) {
            buffer.write(scenarioContent.replace("class=\"scenario\"", "class=\"scenario not-matched\""));
        } else {
            buffer.write(scenarioContent.replace("class=\"scenario\"", "class=\"scenario matched\""));
        }
        buffer.write("</table></div></details></td></tr>\n");
    }

    private static String generateSuggestion(String sourceLine, Set<String>[] targetLines) {
        String[] sourceWords = sourceLine.split("\\s+");
        String bestMatch = "No suitable suggestion found";
        int maxMatches = 0;

        for (Set<String> targetSet : targetLines) {
            for (String targetLine : targetSet) {
                int matches = 0;
                for (String word : sourceWords) {
                    if (targetLine.contains(word)) {
                        matches++;
                    }
                }
                if (matches > maxMatches) {
                    maxMatches = matches;
                    bestMatch = targetLine;
                }
            }
        }
        return bestMatch;
    }
}
////Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/SRC_REGEX.txt
