package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.game.Main;
import group4.graphics.RenderLayer;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL13.glActiveTexture;

public class GraphicsComponent implements Component {

    // color mask that can be set to add this color over every graphics component
    public static Vector3f GLOBAL_COLOR_MASK = new Vector3f();
    // true if the global mask needs to be applied, false otherwise
    public static boolean HAS_MASK = false;

    public VertexArray geometry;
    public Shader shader;
    public Texture texture;

    // Layer/depth at which to render the component.
    public RenderLayer layer;

    // adds color on top of the texture, does nothing unless set explicitly
    public boolean hasMask = false;
    public Vector3f colorMask = new Vector3f();

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
        if (!Main.SHOULD_OPENGL) return;
        this.shader = shader;
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, tcs);
        this.layer = RenderLayer.MAIN;
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
    public GraphicsComponent(Shader shader, Texture texture, float[] vertices, byte[] indices, float[] tcs, RenderLayer layer) {
        if (!Main.SHOULD_OPENGL) return;
        this.shader = shader;
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, tcs);
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
     * @param center    If this is true, the GC will be centered around midpoint the bottom edge
     */
    public GraphicsComponent(Shader shader, Texture texture, Vector3f dimension, boolean center) {
        if (!Main.SHOULD_OPENGL) return;
        // Construct vertex array
        float[] vertices = generateVertices(dimension, center);

        // Construct index array (used for geometry mesh)
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
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, tcs);
        this.layer = RenderLayer.MAIN;
    }

    /**
     * Constructs a VertexArray object and stores the shader and texture for rendering.
     * The vertexArray is created from (0,0,0) till (dimension.x, dimension.y, 0) and is covered fully by the texture.
     * This means the texture will be stretched or shrunk to fit the dimension.
     *
     * @param shader    Shader, the shader to apply during rendering
     * @param texture   Texture, the image to pass to the shader
     * @param dimension size of the graphics to be displayed.
     * @param layer     On what layer to draw
     */
    public GraphicsComponent(Shader shader, Texture texture, Vector3f dimension, RenderLayer layer) {
        if (!Main.SHOULD_OPENGL) return;
        // Construct vertex array
        float[] vertices = generateVertices(dimension, false);

        // Construct index array (used for geometry mesh)
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
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, tcs);
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
     * @param center    If this is true, the GC will be centered around midpoint the bottom edge
     */
    public GraphicsComponent(Shader shader, Texture texture, Vector3f dimension, RenderLayer layer, boolean center) {
        if (!Main.SHOULD_OPENGL) return;
        // Construct vertex array
        float[] vertices = generateVertices(dimension, center);

        // Construct index array (used for geometry mesh)
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
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, tcs);
        this.layer = layer;
    }

    /**
     * Constructs a VertexArray object and stores the shader and texture for rendering.
     * The vertexArray is created from (0,0,0) till (dimension.x, dimension.y, 0) and is covered fully by the texture,
     * though textureCoordinates must be given. This is to work in conjunction with TileMapping, which requires
     * specific coordinates which the GraphicsComponent can't assume knowledge of.
     *
     * @param shader    Shader, the shader to apply during rendering
     * @param texture   Texture, the image to pass to the shader
     * @param dimension Vector3f, size of the graphics to be displayed.
     * @param texCoords Float[], the texture coordinates of a tile within the given tilemap Texture.
     * @param center    If this is true, the GC will be centered around midpoint the bottom edge
     */
    public GraphicsComponent(Shader shader, Texture texture, Vector3f dimension, float[] texCoords, boolean center) {
        if (!Main.SHOULD_OPENGL) return;
        // Construct vertex array
        float[] vertices = generateVertices(dimension, center);

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        // set instance variables
        this.shader = shader;
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, texCoords);
        this.layer = RenderLayer.MAIN;
    }

    /**
     * Constructs a VertexArray object and stores the shader and texture for rendering.
     * The vertexArray is created from (0,0,0) till (dimension.x, dimension.y, 0) and is covered fully by the texture,
     * though textureCoordinates must be given. This is to work in conjunction with TileMapping, which requires
     * specific coordinates which the GraphicsComponent can't assume knowledge of.
     *
     * @param shader    Shader, the shader to apply during rendering
     * @param texture   Texture, the image to pass to the shader
     * @param dimension Vector3f, size of the graphics to be displayed.
     * @param texCoords Float[], the texture coordinates of a tile within the given tilemap Texture.
     * @param layer     Enum, indicating on which specific layer this component should be drawn
     * @param center    If this is true, the GC will be centered around midpoint the bottom edge
     */
    public GraphicsComponent(Shader shader, Texture texture, Vector3f dimension, float[] texCoords, RenderLayer layer, boolean center) {
        if (!Main.SHOULD_OPENGL) return;
        // Construct vertex array
        float[] vertices = generateVertices(dimension, center);

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        // set instance variables
        this.shader = shader;
        this.texture = texture;
        this.geometry = new VertexArray(vertices, indices, texCoords);
        this.layer = layer;
    }


    /**
     * Method to generate a vertex array given a dimension
     *
     * @param dimension the dimension of the bounding rectangle of the resulting vertex array
     * @param center    whether or not to center the vertices around the middle of the bottom edge
     * @return
     */
    private float[] generateVertices(Vector3f dimension, boolean center) {
        if (center) {
            return new float[]{
                    -dimension.x / 2, 0, 0,
                    -dimension.x / 2, dimension.y, 0,
                    dimension.x / 2, dimension.y, 0,
                    dimension.x / 2, 0, 0,
            };
        } else {
            return new float[]{
                    0, 0, 0,
                    0, dimension.y, 0,
                    dimension.x, dimension.y, 0,
                    dimension.x, 0, 0,
            };
        }
    }

    public void setColorMask(Vector3f mask) {
        this.colorMask = new Vector3f(mask);
        hasMask = true;
    }

    public void clearColorMask() {
        this.colorMask = new Vector3f();
        hasMask = false;
    }

    public static void setGlobalColorMask(Vector3f mask) {
        GLOBAL_COLOR_MASK = new Vector3f(mask);
        HAS_MASK = true;
    }

    public static void clearGlobalColorMask() {
        GLOBAL_COLOR_MASK = new Vector3f();
        HAS_MASK = false;
    }

    /**
     * Allows for updating the texture (e.g. for framebased animation)
     * @param texture
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Adds a color on top of the texture of a graphics component.
     * If the graphics component has a personal color that takes priority.
     * Else we look at the global color mask in GraphicsComponent.GLOBAL_COLOR_MASK.
     * If neither are set we have a color mask of 0 which does nothing.
     */
    public void handleColorMask() {
        if (this.hasMask) { // per texture mask has priority
            this.shader.setUniform3f("color_mask", this.colorMask);
        }
    }

    /**
     * Render this GC.
     *
     * @param position Where to render.
     */
    public void render(Vector3f position) {
        if (!Main.SHOULD_OPENGL) return;
        this.shader.bind();
        this.handleColorMask();
        // Set uniforms
        this.shader.setUniformMat4f("md_matrix", Matrix4f.translate(position)); //pc.position.add(new Vector3f(dc.dimension.x / 2.0f, 1.1f * dc.dimension.y, 0.0f))));
        this.shader.setUniform1f("tex", this.texture.getTextureID()); // Specify which texture slot to use

        // Bind texture and specify texture slot
        this.texture.bind();
        glActiveTexture(this.texture.getTextureID());

        this.geometry.render();
    }

    public void flush(Vector3f position) {
        this.render(position);
        this.geometry.deleteBuffers();
    }
}
