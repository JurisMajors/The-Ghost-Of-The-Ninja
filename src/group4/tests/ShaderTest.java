package group4.tests;

import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import group4.utils.ShaderParser;
import group4.utils.BufferUtils;

import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.opengl.GL41.*;

public class ShaderTest {
    VertexArray triangle;

    public ShaderTest() {
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader test = ShaderParser.loadShader("C:\\dev\\DBL-IIS\\src\\group4\\res\\shaders\\simple");
        Texture tex = new Texture("C:\\dev\\DBL-IIS\\src\\group4\\res\\textures\\debug.jpeg");
        float points[] = new float[] {
                0.0f,  0.0f,  0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };
        byte indices[] = new byte[] {0, 1, 2,
                                     0, 2, 3};

        float texCoords[] = new float[] {
                0.0f,  0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f
        };

        triangle = new VertexArray(points, indices, texCoords);
        tex.bind();
        test.bind();
        test.setUniformMat4f("pr_matrix", pr_matrix);
        test.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0, 0, 0)));
        test.setUniform1f("tex", 1);

    }

    public void draw() {
        this.triangle.draw();
    }
}
