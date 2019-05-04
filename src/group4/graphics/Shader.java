package group4.graphics;

import group4.maths.Matrix4f;
import group4.maths.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL41.*;

public class Shader {
    // Storing the attribute locations for in the shader
    public static final int VERTEX_ATTRIBUTE = 0;
    public static final int TEXCOORD_ATTRIBUTE = 1;

    // For storing the program id required for a shader.
    private int shaderId;

    // For remembering the locations of uniforms within the shaders
    private Map<String, Integer> uniformLocationCache = new HashMap<>();

    public Shader(String vertSource, String fragSource) {
        // Create the shader program
        shaderId = glCreateProgram();

        // Create the (sub) shaders
        int vertShader = compileShader(GL_VERTEX_SHADER, vertSource);
        int fragShader = compileShader(GL_FRAGMENT_SHADER, fragSource);

        // Actually load the shaders into the GPU
        glAttachShader(shaderId, vertShader);
        glAttachShader(shaderId, fragShader);
        glLinkProgram(shaderId);
        glValidateProgram(shaderId);
    }

    /**
     * Gives the id of the shader program.
     * @return int, the id.
     */
    public int getShaderId() {
        return this.shaderId;
    }

    /**
     * Sets the shader as the active shader to render with on the GPU.
     */
    public void bind() {
        glUseProgram(shaderId);
    }

    /**
     * Finds the specific location of a given uniform within the shader based on its name.
     *
     * @param name String, the name of the uniform to look up.
     * @return Integer, location of the uniform within the shader. -1 if uniform does not exist.
     */
    private int getUniformLocation(String name) {
        if (uniformLocationCache.containsKey(name)) {
            return uniformLocationCache.get(name);
        }
        int location = glGetUniformLocation(shaderId, name);
        if (location == -1) {
            System.err.printf("[WARNING] Uniform location %s does not exist in Shader %d \n", name, shaderId);
        } else {
            uniformLocationCache.put(name, location);
        }
        return location;
    }

    /**
     * Sets the uniform with the given name to the given value.
     *
     * @param name String, denoting the uniform
     * @param value Integer, value to set
     */
    public void setUniform1i(String name, int value) {
        int location = getUniformLocation(name);
        glUniform1i(location, value);
    }

    /**
     * Sets the uniform with the given name to the given value.
     *
     * @param name String, denoting the uniform
     * @param value Float, value to set
     */
    public void setUniform1f(String name, float value) {
        int location = getUniformLocation(name);
        glUniform1f(location, value);
    }

    /**
     * Sets the uniform with the given name to the given value.
     *
     * @param name String, denoting the uniform
     * @param v1 Float, first value to set
     * @param v2 Float, second value to set
     */
    public void setUniform2f(String name, float v1, float v2) {
        int location = getUniformLocation(name);
        glUniform2f(location, v1, v2);
    }

    /**
     * Sets the uniform with the given name to the given value.
     *
     * @param name String, denoting the uniform
     * @param vector Vector3f, value to set
     */
    public void setUniform3f(String name, Vector3f vector) {
        int location = getUniformLocation(name);
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    /**
     * Sets the uniform with the given name to the given value.
     *
     * @param name String, denoting the uniform
     * @param matrix Matrix4f, value to set
     */
    public void setUniformMat4f(String name, Matrix4f matrix) {
        int location = getUniformLocation(name);
        glUniformMatrix4fv(location, false, matrix.toFloatBuffer());
    }
    /**
     * Given a shader type and the source code of the shader, this function creates and compiles
     * the appropriate shader for use in OpenGL.
     *
     * @param type (GL_VERTEX_SHADER | GL_FRAGMENT_SHADER)
     * @param source String with the source code of the shader to be created
     * @return id of shader if compiled successfully, 0 if compilation failed.
     */
    private int compileShader(int type, String source) {
        int id = glCreateShader(type);  // Create a default shader of given type
        glShaderSource(id, source);     // Overwrite the sourcecode of the shader
        glCompileShader(id);            // Compile the newly written sourcecode

        // Get the compilation status.
        int[] result = new int[1];  // sic.
        glGetShaderiv(id, GL_COMPILE_STATUS, result);

        // If compilation failed...
        if (result[0] == GL_FALSE) {
            System.err.printf("Could not compile %s\n", type);
            System.err.println(glGetShaderInfoLog(id));
            id = 0; // 0 so we can graciously fail and keep running.
        }
        return id;
    }
}
