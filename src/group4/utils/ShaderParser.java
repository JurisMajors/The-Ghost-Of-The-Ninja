package group4.utils;

import group4.graphics.Shader;

import java.io.File;
import java.io.IOException;

public class ShaderParser {

    // String sources used during shader loading
    private static String vertexSource;
    private static String fragmentSource;

    /**
     * Loads the shader(s) located in the shaderDirectory. Example:
     * /path/to/shader_a/a.vert
     * /path/to/shader_a/a.frag
     * In this case shaderDirectory would be /path/to/shader_a/
     *
     * @param shaderDirectory the directory holding the files for this shader
     * @return Shader object
     */
    public static Shader loadShader(String shaderDirectory) {
        // get all files in the given directory
        File directory = new File(shaderDirectory);
        File[] listOfFiles = directory.listFiles();

        // empty the shader sources
        vertexSource = "";
        fragmentSource = "";

        for (File file : listOfFiles) {
            // we only deal with files, not directories
            if (file.isFile()) {
                handleFile(file);
            } else {
                System.err.println("[WARNING] non file object found in shader directory");
            }
        }

        // this directory does not contain enough shaders
        if (vertexSource.equals("") || fragmentSource.equals("")) {
            System.err.println("[ERROR] this directory does not have a vertex and a fragment shader");
            throw new IllegalArgumentException("Directory does not contain enough shaders");
        }

        // create and return shader
        return new Shader(vertexSource, fragmentSource);
    }

    /**
     * Checks if a file is a shader file.
     * If the given file is a shader file, read its source code and set it to the appropriate source variable.
     *
     * @param file input file
     */
    private static void handleFile(File file) {
        // find out which type of file we are dealing with
        String prevSource;
        String shaderType;

        // we currently only allow vertex and fragent shaders
        if (endsWith(file.getName(), ".vert")) {
            prevSource = vertexSource;
            shaderType = "vertex";
        } else if (endsWith(file.getName(), ".frag")) {
            prevSource = fragmentSource;
            shaderType = "fragment";
        } else {
            System.err.println("[WARNING] non-shader file found in " + file.getParent());
            return;
        }

        // if this shader type already has source code, we ignore this file
        if (!prevSource.equals("")) {
            System.err.println("[WARNING] Multiple " + shaderType + " shaders found in " + file.getParent() +
                    ". Using the first one found.");
            return;
        } else {
            // read the shader source code into the correct source variable
            try {
                if (shaderType.equals("vertex")) {
                    vertexSource = FileUtils.readFileToString(file.getAbsolutePath());
                } else if (shaderType.equals("fragment")) {
                    fragmentSource = FileUtils.readFileToString(file.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("[ERROR] unable to open " + file.getAbsolutePath() + " for reading");
            }
        }
    }


    /**
     * Checks if a given string ends with a given other string.
     *
     * @param string strint to check
     * @param end    end
     * @return true if string ends in end
     */
    private static boolean endsWith(String string, String end) {
        if (string.length() < end.length()) {
            return false;
        }

        int start = string.length() - end.length();
        return string.substring(start).equals(end);
    }

}