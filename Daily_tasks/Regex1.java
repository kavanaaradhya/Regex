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
        //This line creates a new Scanner object named scanner, which is used to read input from the standard input stream (System.in).
        //This allows the user to input data from the console

        System.out.println("Enter the path to the source file:");

        String sourceFilePath = scanner.nextLine();
        //This line reads the next line of input from the user using the Scanner object scanner and assigns it to the variable sourceFilePath.
        //This will be the path to the source file provided by the user.

        System.out.println("\u001B[31m::::::: :::::::: G E N E R A T I N G - R E P O R T :::::: :::::::::\u001B[0m");
        Thread.sleep(2000);

        generateReport(sourceFilePath);
        //This line calls the generateReport method, passing the sourceFilePath variable as an argument.
        //This method is expected to generate a report based on the provided source file path.
    }

    private static void generateReport(String sourceFilePath) {
        try {
            Set<String>[] targetLines = new HashSet[TARGET_FILE_PATHS.length];
            //This line declares an array named targetLines which is an array of Sets of Strings.
            //The array length is the same as the length of the TARGET_FILE_PATHS array.

            for (int i = 0; i < TARGET_FILE_PATHS.length; i++) {
                //This line starts a for loop that iterates through each element of the TARGET_FILE_PATHS array.

                targetLines[i] = readLinesFromFile(TARGET_FILE_PATHS[i]);
                // Inside the loop, this line assigns the result of the readLinesFromFile method for the current file path to the corresponding index of the targetLines array.
            }

            FileWriter writer = new FileWriter(HTML_FILE_PATH);
            // This line creates a FileWriter object named writer which will be used to
            //write data to the file specified by the HTML_FILE_PATH constant.

            BufferedWriter buffer = new BufferedWriter(writer);
            //This line creates a BufferedWriter object named buffer which is used to efficiently write characters to the file.

            buffer.write(generateHtmlHeader());
            //This line calls the method generateHtmlHeader() which is expected to return a String representing the HTML header,
            //and writes this header to the file via the buffer.

            try (BufferedReader br = new BufferedReader(new FileReader(sourceFilePath)))
            //This line starts a try-with-resources block. It creates a BufferedReader named br that reads from the file specified by sourceFilePath.
            //The try-with-resources statement ensures that the BufferedReader is properly closed after the block is executed.
            {
                String line;
                //This line declares a String variable named line to store each line read from the file.
                while ((line = br.readLine()) != null)
                //This line starts a while loop that continues as long as the readLine() method of the BufferedReader returns a non-null value
                //(indicating there are more lines to read).
                {
                    line = line.trim();
                    //This line removes any leading or trailing whitespace from the current line.

                    if (!line.isEmpty())
                    //This line checks if the trimmed line is not empty.
                    {
                        line = line.replaceAll("(?i)\\b(given|then|when|and)\\b", " ");
                        //This line replaces any occurrence of the words "given", "then", "when", or "and"
                        //(case-insensitive) surrounded by word boundaries with a single space.

                        boolean matched = false;

                        for (int i = 0; i < TARGET_FILE_PATHS.length; i++) {
                            //This line starts a loop to iterate through the target files.

                            for (String targetLine : targetLines[i]) {
                                //This line starts a loop to iterate through each target line in the current target file.

                                targetLine = targetLine.replaceAll("\\b(Given|When|Then)\\b", " ");
                                //This line replaces any occurrence of the words "Given", "When", or "Then"
                                //surrounded by word boundaries with a single space in the current target line.

                                Pattern pattern = Pattern.compile(targetLine);
                                //This line compiles the current target line into a regular expression pattern.

                                Matcher matcher = pattern.matcher(line);
                                //This line creates a matcher object to match the current line against the target line pattern.

                                if (matcher.matches()) {
                                    //This line checks if the current line matches the target line pattern.

                                    buffer.write("<tr class=\"matched\"><td>" + line + "</td><td>" + targetLine + "</td><td>Matched</td></tr>\n");
                                    //This line writes HTML code to the output buffer indicating that the current line matches the target line.

                                    matched = true;
                                    //This line sets the matched variable to true, indicating that the current line has been matched.

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
    }///Users/kavana/Documents/Workspace1/Regex_validation/src/test/java/Daily_tasks/Source_file.txt
}/*this method reads each line from the file, trims it, removes any occurrences of certain keywords,
and adds the processed line to a set. Finally, it returns the set containing all the processed lines.*/
