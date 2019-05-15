package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

import java.util.ArrayList;


public class SplinePlatform extends Entity {

    /**
     * Creates a platform based on a spline.
     * Note that all the points of the spline argument MUST be inside the dimension vector.
     *
     * @param position  left-bottom-back corner of the cuboid representing the platform
     * @param dimension such that the right-top-front corner of the cuboid representing the platform is position+dimension
     * @param spline    spline representing the actual walkable platform
     * @param shader    shader for this platform
     * @param texture   texture for this platform
     */
    public SplinePlatform(Vector3f position, Vector3f dimension, MultiSpline spline, Shader shader, Texture texture) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new PlatformComponent());
        this.add(new ColliderComponent());

        GraphicsComponent gc = createGraphicsComponent(spline, 100, shader, texture);
        this.add(gc);
    }

    private GraphicsComponent createGraphicsComponent(MultiSpline spline, int numPoints, Shader shader, Texture texture) {

        float dt = 1 / (float) numPoints;
        // temporary arraylists to store the vertex cooridnates, texture coordinates, indices
        ArrayList<Vector3f> vertexArray = new ArrayList<>();
        ArrayList<Vector3f> tcArray = new ArrayList<>();
        ArrayList<Byte> indices = new ArrayList<>();

        Vector3f[] lefts = new Vector3f[numPoints + 1];
        Vector3f[] rights = new Vector3f[numPoints + 1];

        // keep track of at which index we are to make sure that the indices align properly
        byte pointI = 0;
        byte vertexI = 0;

        float t = 0.0f;
        for (int k = 0; k <= numPoints; k++) {
            Vector3f splinePoint = spline.getPoint(t);
            Vector3f normal = spline.getNormal(t);

            // get points on the borders of the spline
            Vector3f left = splinePoint.sub(normal).normalized().scale(0.5f);
            Vector3f right = splinePoint.add(normal).normalized().scale(0.5f);

            lefts[k] = left;
            rights[k] = right;


            t += dt;
        }

        vertexArray.add(lefts[0]);
        vertexArray.add(rights[0]);
        tcArray.add(new Vector3f());
        tcArray.add(new Vector3f());
        for (int k = 0; k < numPoints; k++) {
            vertexArray.add(lefts[k + 1]);
            vertexArray.add(rights[k + 1]);
            // TODO: for now always botleft
            tcArray.add(new Vector3f());
            tcArray.add(new Vector3f());

            // first triangle (CW)
            indices.add((byte) (2 * k)); // first left
            indices.add((byte) (2 * k + 2)); // second left
            indices.add((byte) (2 * k + 3)); // second right

            // second triangle (CW)
            indices.add((byte) (2 * k + 3)); // second right
            indices.add((byte) (2 * k + 1)); // first right
            indices.add((byte) (2 * k)); // first left
        }

        // turn the arraylists into arrays for the VertexArray object
        float[] va = createVertexArray(vertexArray);
        float[] tc = createTextureArray(tcArray);
        byte[] ic = createIndicesArray(indices);

        return new GraphicsComponent(shader, texture, va, ic, tc);

    }


    // TODO: move all the functions below since I copy pasted them for quick testing

    /**
     * Creates float array from a vector3f arraylist for the vertexArray class.
     * For each vector in the list it adds x, y, z separately to the float array.
     *
     * @param al arraylist with vector3f
     * @return float array with (local/model) vertex coordinates
     */
    private float[] createVertexArray(ArrayList<Vector3f> al) {
        float[] result = new float[al.size() * 3];
        // add the x y z as a separate element
        for (int i = 0; i < al.size(); i++) {
            result[3 * i + 0] = al.get(i).x;
            result[3 * i + 1] = al.get(i).y;
            result[3 * i + 2] = al.get(i).z;
        }
        return result;
    }

    /**
     * Creates float array from a vector3f arraylist for texture coordinates.
     * For each vector in the list it adds the x, y separately to the float array.
     *
     * @param al
     * @return float array with texture coordinates
     */
    private float[] createTextureArray(ArrayList<Vector3f> al) {
        float[] result = new float[al.size() * 2];
        // add the x y z as a separate element
        for (int i = 0; i < al.size(); i++) {
            result[2 * i + 0] = al.get(i).x;
            result[2 * i + 1] = al.get(i).y;
        }
        return result;
    }

    private byte[] createIndicesArray(ArrayList<Byte> al) {
        byte[] result = new byte[al.size()];
        for (int i = 0; i < al.size(); i++) {
            result[i] = al.get(i);
        }
        return result;
    }

}
