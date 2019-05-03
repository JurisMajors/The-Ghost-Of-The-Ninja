package group4.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

}
