package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.utils.ShaderParser;

public class GraphicsComponent implements Component {

    // TODO: how to represent graphics
    // Walk
    // Death
    // Attack
    // Jump
    // Fall
    // getAttacked
    // Maybe combine with physics

    public VertexArray triangle;
    public Shader shader;
    public Texture texture;
    public int layer;

    public GraphicsComponent(Shader shader, Texture texture, float[] vertices, byte[] indices, float[] tcs) {
        this.shader = shader;
        this.texture = texture;
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.layer = 0;
    }

    public GraphicsComponent(Shader shader, Texture texture, float[] vertices, byte[] indices, float[] tcs, int layer) {
        this.shader = shader;
        this.texture = texture;
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.layer = layer;
    }

}
