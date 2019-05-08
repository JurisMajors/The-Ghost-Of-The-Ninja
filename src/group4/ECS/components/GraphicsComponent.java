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

    public GraphicsComponent(String shader, String texture, float[] vertices, byte[] indices, float[] tcs) {
        this.shader = ShaderParser.loadShader(shader);
        this.texture = new Texture(texture);
        this.triangle = new VertexArray(vertices, indices, tcs);
    }

}
