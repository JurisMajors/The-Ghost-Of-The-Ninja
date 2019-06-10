package group4.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    // make sure nobody makes a FileUtils object
    private FileUtils() {
    }

    /**
     * Reads a file located at filePath and puts its contents into a string.
     *
     * @param filePath path to file
     * @return string of the given file
     */
    public static String readFileToString(String filePath) throws IOException {
        // open file for reading
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        StringBuilder result = new StringBuilder();

        // read file per line
        String line;
        while ((line = reader.readLine()) != null) {
            // add this line to the result string
            result.append(line);
            result.append('\n');
        }

        return result.toString();
    }

    /**
     * Gives all files in a given folder.
     * @param folder
     * @return A list of strings.
     */
    public static List<String> getFilePaths(String folder) {
        List<String> filePaths = new ArrayList<>();
        File folder_fd = new File(folder);
        for (final File fileEntry : folder_fd.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                filePaths.add(fileEntry.getPath());
            }
        }
        Collections.sort(filePaths); // sort the modules in the correct order
        return filePaths;
    }
}
