package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.graphics.RenderLayer.Layer;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Vector3f;

import java.util.ArrayList;

public class GraphicsComponent implements Component {

    public VertexArray triangle;
    public Shader shader;
    public Texture texture;

    // Layer/depth at which to render the component.
    //      -1 : background layer   (e.g. background art)
    //       0 : main layer         (e.g. player, platform, enemies)
    //       1 : foreground layer   (e.g. foreground art, falling (snow/fire/water) particle effects)
    //       2 : fx layer           (e.g. post-processing)
    public Layer layer;

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
        this.layer = Layer.MAIN;
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
    public GraphicsComponent(Shader shader, Texture texture, float[] vertices, byte[] indices, float[] tcs, Layer layer) {
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
        this.texture = texture;
        this.triangle = new VertexArray(vertices, indices, tcs);
        this.layer = Layer.MAIN;
    }

//    public static GraphicsComponent createGraphicsComponent(Vector3f position, Vector3f dimension, Texture texture, Shader shader) {
//        int texWidth = texture.getWidth();
//        int texHeight = texture.getHeight();
//
//        // boundaries for the platform on the top and right (local)
//        float topY = (dimension).y;
//        float rightX = (dimension).x;
//
//        // temporary arraylists to store the vertex cooridnates, texture coordinates, indices
//        ArrayList<Vector3f> vertexArray = new ArrayList<>();
//        ArrayList<Vector3f> tcArray = new ArrayList<>();
//        ArrayList<Byte> indices = new ArrayList<>();
//
//        // keep track of at which index we are to make sure that the indices align properly
//        byte startI = 0;
//        // loop over all 'texture sized' blocks in this platform
//        for (int x = 0; x <= rightX - 1; x += 1) {
//            for (int y = 0; y <= topY - 1; y += 1) {
//                // find the end x and y for the current texture block
//                // TODO: remove
//                float endY = Math.min(topY, y + 1);
//                endY = y + 1;
//                float endX = Math.min(rightX, x + 1);
//                endX = x + 1;
//
//                // add a texture block with bottomleft(x,y) and topright(x+endX, y+endY)
//                // bottomleft
//                Vector3f blVertex = new Vector3f(x, y, 0.0f);
//                Vector3f blTc = new Vector3f(0, 1, 0);
//                // topleft
//                Vector3f tlVertex = new Vector3f(x, endY, 0.0f);
////                Vector3f tlTc = new Vector3f(x, (endY - y) / texHeight, 0);
//                Vector3f tlTc = new Vector3f(0, 0, 0);
//                // topright
//                Vector3f trVertex = new Vector3f(endX, endY, 0.0f);
////                Vector3f trTc = new Vector3f((endX - x) / texWidth, (endY - y) / texHeight, 0);
//                Vector3f trTc = new Vector3f(1, 0, 0);
//                // bottomright
//                Vector3f brVertex = new Vector3f(endX, y, 0.0f);
////                Vector3f brTc = new Vector3f((endX - x) / texWidth, 0, 0);
//                Vector3f brTc = new Vector3f(1, 1, 0);
//
//                // add the vertices and texture coordinates
//                vertexArray.add(blVertex);
//                vertexArray.add(tlVertex);
//                vertexArray.add(trVertex);
//                vertexArray.add(brVertex);
//                tcArray.add(blTc);
//                tcArray.add(tlTc);
//                tcArray.add(trTc);
//                tcArray.add(brTc);
//
//                // add the correct indices to make a square out of two triangles
//                // first triangle
//                indices.add(startI);
//                indices.add((byte) (startI + 1));
//                indices.add((byte) (startI + 2));
//                // second triangle
//                indices.add((byte) (startI + 2));
//                indices.add((byte) (startI + 3));
//                indices.add(startI);
//
//                // we added 4 vertices so the index needs to be updated with +4
//                startI += 4;
//            }
//        }
//
//        // turn the arraylists into arrays for the VertexArray object
//        float[] va = createVertexArray(vertexArray);
//        float[] tc = createTextureArray(tcArray);
//        byte[] ic = createIndicesArray(indices);
//
//        return new GraphicsComponent(shader, texture, va, ic, tc);
//    }
}
