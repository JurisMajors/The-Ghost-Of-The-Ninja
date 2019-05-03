package group4.graphics;

import static org.lwjgl.opengl.GL41.*;

public class Shader {
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

    public int getShaderId() {
        return this.shaderId;
    }

    void bind() {
        glUseProgram(shaderId);
    }

    void unbind() {
        glUseProgram(0);
    }

    // TODO: setUniform functions. Which we currently might not even need.

    int getUniformLocation(String name) {
        // TODO: Cache the locations if we start doing this call often.
        int location = glGetUniformLocation(shaderId, name);
        if (location == -1) {
            System.err.printf("[WARNING] Uniform location %s does not exist in Shader %d \n", name, shaderId);
        }
        return location;
    }

    private int compileShader(int type, String source) {
        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);

        int[] result = new int[1];
        glGetShaderiv(id, GL_COMPILE_STATUS, result);
        if (result[0] == GL_FALSE) {
            System.err.printf("Could not compile %s\n", type);
            System.err.println(glGetShaderInfoLog(id));
            id = 0; // 0 so we can graciously fail and keep running.
        }
        return id;
    }
}
