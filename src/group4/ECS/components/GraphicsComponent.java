package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Vector3f;

public class GraphicsComponent implements Component {

    public VertexArray triangle;
    public Shader shader;
    public Texture texture;

    // Layer/depth at which to render the component.
    //      -1 : background layer   (e.g. background art)
    //       0 : main layer         (e.g. player, platform, enemies)
    //       1 : foreground layer   (e.g. foreground art, falling (snow/fire/water) particle effects)
    //       2 : fx layer           (e.g. post-processing)
    public int layer; // TODO: An enum could be nice here

    /**
     * Constructor which constructs a VertexArray object and stores the given shader and texture for rendering. Gives
     * the resulting GraphicsComponent layer 0 (= main layer).
     *
     * @param shader   Shader, the shader to apply during rendering
     * @param texture  Texture, the image to pass to the shader
     * @param vertices Float[], all the vertex positions
     * @param indices  Byte[], the order in which vertices should be (re)-used. `indices.length` must be multiple of 3!
     * @param tcs      Float[], a uv/st-coordinate for every vertex.
     */
    public GraphicsComponent(Shader shader, Texture texture, float[] vertices, byte[] indices, float[] tcs) {
        this.shader = shader;
        this.texture = texture;
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.layer = 0;
    }

    /**
     * Constructor which constructs a VertexArray object and stores the given shader and texture for rendering. Gives
     * the resulting GraphicsComponent the specified layer to render to.
     *
     * @param shader   Shader, the shader to apply during rendering
     * @param texture  Texture, the image to pass to the shader
     * @param vertices Float[], all the vertex positions
     * @param indices  Byte[], the order in which vertices should be (re)-used. `indices.length` must be multiple of 3!
     * @param tcs      Float[], a uv/st-coordinate for every vertex.
     * @param layer    Integer, specifying the layer depth at which the component should be rendered.
     */
    public GraphicsComponent(Shader shader, Texture texture, float[] vertices, byte[] indices, float[] tcs, int layer) {
        this.shader = shader;
        this.texture = texture;
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.layer = layer;
    }

    /**
     * Constructs a VertexArray object and stores the shader and texture for rendering.
     * The vertexArray is created from (0,0,0) till (dimension.x, dimension.y, 0) and is covered fully by the texture.
     * This means the texture will be stretched or shrunk to fit the dimension.
     *
     * @param shader    Shader, the shader to apply during rendering
     * @param texture   Texture, the image to pass to the shader
     * @param dimension size of the graphics to be displayed.
     */
    public GraphicsComponent(Shader shader, Texture texture, Vector3f dimension) {
       // Construct vertex array
        float[] vertices = new float[]{
                0, 0, 0,
                0, dimension.y, 0,
                dimension.x, dimension.y, 0,
                dimension.x, 0, 0,
        };

        // Construct index array (used for triangle mesh)
        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        // Construct texture coords covering the full texture
        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        // set instance variables
        this.shader = shader;
        this.texture =texture;
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.layer = 0;
    }

}
