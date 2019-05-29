package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.SplineComponent;
import group4.ECS.components.identities.PlatformComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.systems.collision.CollisionHandlers.SplinePlatformCollision;
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
    public SplinePlatform(Vector3f position, Vector3f dimension, MultiSpline spline, float thickness, Shader shader, Texture texture) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new PlatformComponent());
        this.add(new CollisionComponent(SplinePlatformCollision.getInstance()));

        SplineComponent sp = createSplineComponent(spline, 100, thickness);
        this.add(sp);

        GraphicsComponent gc = createGraphicsComponent(sp.points, sp.normals, thickness, shader, texture);
        this.add(gc);
    }

    private SplineComponent createSplineComponent(MultiSpline spline, int numPoints, float thickness) {
        Vector3f[] points = new Vector3f[numPoints + 1];
        Vector3f[] normals = new Vector3f[numPoints + 1];
        // dt determines the step of time
        float dt = 1 / (float) numPoints;
        // t is the time with which we go over the spline
        float t = 0.0f;
        // loop through all controlpoints over the spline
        for (int k = 0; k <= numPoints; k++) {
            if (t > 1.0f) t = 1.0f;

            points[k] = spline.getPoint(t);
            normals[k] = spline.getNormal(t);

            // update time
            t += dt;
        }

        return new SplineComponent(points, normals, thickness);
    }

    /**
     * Creates the graphics component for a spline platform.
     * The vertex mesh is places around the spline points, the eventual spline will have the given thickness.
     * The mesh will extend 0.5 * thickness above the spline points and 0.5 * thickness below the spline points.
     * NOTE: if you want to align the spline nicely, it is important to make sure that the first controlPoint of your
     * spline += 0.5 * thickness is at the top or bottom of your dimension component.
     * The same holds for your last control point.
     *
     * @param points    points of the spline
     * @param normals   normals of the spline
     * @param thickness thickness of the spline
     * @param shader    shader
     * @param texture   texture
     * @return graphics component
     */
    private GraphicsComponent createGraphicsComponent(Vector3f[] points, Vector3f[] normals, float thickness, Shader shader, Texture texture) {
        // temporary arraylists to store the vertex cooridnates, texture coordinates, indices
        ArrayList<Vector3f> vertexArray = new ArrayList<>();
        ArrayList<Vector3f> tcArray = new ArrayList<>();
        ArrayList<Byte> indices = new ArrayList<>();

        // store a list of the top and bottom points (around the spline control points)
        Vector3f[] tops = new Vector3f[points.length];
        Vector3f[] bots = new Vector3f[points.length];

        // loop through all controlpoints over the spline
        for (int k = 0; k < points.length; k++) {
            Vector3f splinePoint = points[k];
            Vector3f normal = normals[k];

            // get points on the borders of the spline
            Vector3f top = splinePoint.sub(normal.scale(0.5f * thickness));
            Vector3f bot = splinePoint.add(normal.scale(0.5f * thickness));

            tops[k] = top;
            bots[k] = bot;
        }

        // add all tops and bots to the vertex array in order: (top0, bot0, top1, bot1, ...)
        vertexArray.add(tops[0]);
        vertexArray.add(bots[0]);
        // add texture coordinates for all the vertices
        tcArray.add(new Vector3f());
        tcArray.add(new Vector3f());
        for (int k = 0; k < points.length - 1; k++) {
            // add next vertex coordinates and texture coordinates
            vertexArray.add(tops[k + 1]);
            vertexArray.add(bots[k + 1]);
            tcArray.add(new Vector3f());
            tcArray.add(new Vector3f());

            // create a square with the previous top and bot and the current top and bot
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

        // create graphics component
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

    public static String getName() {
        return "SplinePlatform";
    }
}
