package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class SplinePathComponent implements Component {

    //    private MultiSpline spline;
    private Vector3f dimension;
    private Vector3f position;
    private float visionRange;

    public Vector3f[] points;

    /**
     * Creates a spline path component representing the path that a mob should follow.
     *
     * @param spline      Spline to follow, should be circular.
     * @param position    A rectangle that covers the entire spline in width and height.
     * @param dimension   The world position of the bottom left point of the spline (and the dimension).
     * @param visionRange The maximum distance an entity can have from this spline to see it.
     * @param detail      The amount of points that should approximate the spline.
     */
    public SplinePathComponent(MultiSpline spline, Vector3f position, Vector3f dimension, float visionRange, int detail) {
//        this.spline = spline;
        this.position = position;
        this.dimension = dimension;
        this.visionRange = visionRange;
        createSplinePoints(spline, position, detail);
    }

    /**
     * Fills the points according to the number of points that should approximate this spline.
     *
     * @param spline    the spline itself
     * @param position  position of bottom left of the spline in world space
     * @param numPoints the amount of points that should approximate this spline
     */
    private void createSplinePoints(MultiSpline spline, Vector3f position, int numPoints) {
        points = new Vector3f[numPoints + 1];
//        Vector3f[] normals = new Vector3f[numPoints + 1];
        // dt determines the step of time
        float dt = 1 / (float) numPoints;
        // t is the time with which we go over the spline
        float t = 0.0f;
        // loop through all controlpoints over the spline
        for (int k = 0; k <= numPoints; k++) {
            points[k] = spline.getPoint(t).add(position);
//            normals[k] = spline.getNormal(t);

            // update time
            t += dt;
        }
    }


}
