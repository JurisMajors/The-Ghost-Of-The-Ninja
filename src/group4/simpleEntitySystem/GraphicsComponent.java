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

    private Entity entity;
    private VertexArray triangle;
    private Shader shader;
    private Texture texture;
    private Vector3f position;

    public GraphicsComponent(Entity entity, String shader, String texture, float[] vertices, byte[] indices, float[] tcs, Vector3f position) {
        this.entity = entity;
        this.shader = ShaderParser.loadShader(shader);
        this.texture = new Texture(texture);
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.position = position;
    }

    public void updatePosition(Vector3f newPos) {
        this.position = newPos;
    }

    public void render() {
        this.shader.bind();
        this.shader.setUniformMat4f("pr_matrix", this.entity.getModule().getProjectionMatrix());
        this.shader.setUniformMat4f("vw_matrix", Matrix4f.translate(this.position));
        this.shader.setUniform1f("tex", 1);
        this.texture.bind();
        glActiveTexture(GL_TEXTURE1);
        this.triangle.render();
    }
}
