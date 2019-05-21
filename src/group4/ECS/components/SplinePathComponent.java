package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class SplinePathComponent implements Component {

    private MultiSpline spline;
    private Vector3f dimension;
    private Vector3f position;
    private float visionRange;

    private Vector3f[] points;

    /**
     * Creates a spline path component representing the path that a mob should follow.
     * @param spline Spline to follow, should be circular.
     * @param position A rectangle that covers the entire spline in width and height.
     * @param dimension The world position of the bottom left point of the spline (and the dimension).
     * @param visionRange The maximum distance an entity can have from this spline to see it.
     */
    public SplinePathComponent(MultiSpline spline, Vector3f position, Vector3f dimension, float visionRange, int detail) {
        this.spline = spline;
        this.position = position;
        this.dimension = dimension;
        this.visionRange = visionRange;
    }

    public Vector3f targetPosition(Vector3f currentPosition) {

        return null;

    }

    private Vector3f getClosestSplinePoint(Vector3f position) {
        return null;

    }


}
