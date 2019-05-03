package group4.graphics;

import static org.lwjgl.opengl.GL41.*;

public class Shader {
    private int shaderId;

    public Shader(String vertSource, String fragSource) {
        shaderId = glCreateProgram();
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
    }

    void bind() {
        glUseProgram(shaderId);
    }

    void unbind() {
        glUseProgram(0);
    }

    int getUniformLocation(String name) {
        int location = glGetUniformLocation(shaderId, name);
        if (location == -1) {
            System.err.printf("[WARNING] Uniform location %s does not exist in Shader %d \n", name, shaderId);
        }
        return location;
    }

    private long compileShader() {
        glCreateShader(GL_VERTEX_SHADER, vertexSource);
    }
}
