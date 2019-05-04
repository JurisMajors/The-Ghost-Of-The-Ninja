package group4.tests;

import group4.graphics.Shader;
import group4.maths.Matrix4f;
import group4.utils.ShaderParser;
import group4.utils.BufferUtils;

import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.opengl.GL41.*;

public class ShaderTest {
    public ShaderTest() {
        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader test = ShaderParser.loadShader("C:\\dev\\DBL-IIS\\src\\group4\\res\\shaders\\simple");
        float points[] = {
                0.0f,  1.0f,  0.0f,
                1.0f, -1.0f,  0.0f,
                -1.0f, -1.0f,  0.0f
        };
        byte indices[] = {0, 1, 2};
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.getFloatBuffer(points), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);
        glEnableVertexAttribArray(0);

        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.getByteBuffer(indices), GL_STATIC_DRAW);

        // clear buffers again
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        test.bind();
        test.setUniformMat4f("pr_matrix", pr_matrix);

        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

        glDrawElements(GL_TRIANGLES, points.length, GL_UNSIGNED_BYTE, 0);
    }
}
