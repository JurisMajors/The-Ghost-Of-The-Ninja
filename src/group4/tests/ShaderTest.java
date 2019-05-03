package group4.tests;

import group4.graphics.Shader;
import group4.maths.Matrix4f;

import static org.lwjgl.opengl.GL41.*;

public class ShaderTest {
    public ShaderTest() {
        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
//        Shader test = new Shader("#version 330 core")

        glBegin(GL_QUADS);
        glVertex3f(-1.0f, 1.0f, 0.0f); // top left
        glVertex3f(1.0f, 1.0f, 0.0f); // top right
        glVertex3f(1.0f, -1.0f, 0.0f); // bottom right
        glVertex3f(-1.0f, -1.0f, 0.0f); // bottom left
        glEnd();
    }
}
