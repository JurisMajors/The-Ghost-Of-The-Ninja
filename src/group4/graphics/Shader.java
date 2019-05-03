package group4.graphics;

import static org.lwjgl.opengl.GL41.*;

public class Shader {
    // For storing the program ids required for a shader.
    private int shaderId;
    private int vertShader;
    private int fragShader;

    public Shader(String vertSource, String fragSource) {
        // Create the shader program
        shaderId = glCreateProgram();

        // Create the (sub) shaders
        vertShader = compileShader(GL_VERTEX_SHADER, vertSource);
        fragShader = compileShader(GL_FRAGMENT_SHADER, fragSource);

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
     * Sets the default shader (0) as the active shader to render with on the GPU.
     */
    public void unbind() {
        glUseProgram(0);
    }

    // TODO: setUniform functions. Which we currently might not even need.

    /**
     * Finds the specific location of a given uniform within the shader based on its name.
     *
     * @param name String, the name of the uniform to look up.
     * @return int, location of the uniform within the shader. -1 if uniform does not exist.
     */
    private int getUniformLocation(String name) {
        // TODO: Cache the locations if we start doing this call often.
        int location = glGetUniformLocation(shaderId, name);
        if (location == -1) {
            System.err.printf("[WARNING] Uniform location %s does not exist in Shader %d \n", name, shaderId);
        }
        return location;
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
