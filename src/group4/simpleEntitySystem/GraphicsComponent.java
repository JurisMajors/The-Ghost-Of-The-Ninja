package group4.simpleEntitySystem;

import group4.game.Main;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import group4.utils.ShaderParser;

import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class GraphicsComponent {

    private VertexArray _triangle;
    private Shader _shader;
    private Texture _texture;
    private Vector3f _position;

    public GraphicsComponent(String shader, String texture, float[] vertices, byte[] indices, float[] tcs, Vector3f position) {
        this._shader = ShaderParser.loadShader(shader);
        this._texture = new Texture(texture);
        this._triangle = new VertexArray(vertices, indices, tcs);
        this._position = position;
    }

    public void _updatePosition(Vector3f newPos) {
        this._position = newPos;
    }

    public void _render() {
        this._shader.bind();
        this._shader.setUniformMat4f("pr_matrix", Main.pr_matrix);
        this._shader.setUniformMat4f("vw_matrix", Matrix4f.translate(this._position));
        this._shader.setUniform1f("tex", 1);
        this._texture.bind();
        glActiveTexture(GL_TEXTURE1);
        this._triangle.render();
    }
}
