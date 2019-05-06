package group4.simpleEntitySystem;

import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import group4.utils.ShaderParser;

import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class GraphicsComponent {

    private VertexArray triangle;

    public GraphicsComponent() {
        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader test = ShaderParser.loadShader("src/group4/res/shaders/simple");
        Texture tex = new Texture("src/group4/res/textures/debug.jpeg");
        float[] vertices = new float[] {
                -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
                -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                0.0f, -10.0f * 9.0f / 16.0f, 0.0f
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        triangle = new VertexArray(vertices, indices, tcs);
        test.bind();
        tex.bind();
        glActiveTexture(GL_TEXTURE1);

        test.setUniformMat4f("pr_matrix", pr_matrix);
        test.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0, 0, 0)));
        test.setUniform1f("tex", 1);

    }

    public void _render() {
        this.triangle.render();
    }
}
