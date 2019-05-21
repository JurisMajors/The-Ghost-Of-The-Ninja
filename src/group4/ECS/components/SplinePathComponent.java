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
     * Gets the direction for a given entity to move towards if it wants to move towards the spline.
     *
     * @param entityPosition  bl position of the entity
     * @param entityDimension dimension of the entity
     * @param entityVelocity  velocity of the entity
     * @return
     */
    public Vector3f targetDirection(Vector3f entityPosition, Vector3f entityDimension, Vector3f entityVelocity) {
        Vector3f targetDirection = null;
        int offset = 5;

        Vector3f closestPoint = getClosestSplinePoint(entityPosition, entityDimension);
        float distance = entityPosition.sub(closestPoint).length();

        // if the entity is almost on the spline, follow the spline path
        if (distance < 0.5f) {
            // get the next and previous point on the spline
            int index = getClosestSplinePointIndex(entityPosition, entityDimension);
            Vector3f nextPoint = points[(index + 5) % points.length];
            Vector3f prevPoint = points[(index - 5 + points.length) % points.length];

            // get the corresponding directions
            Vector3f forwardDirection = nextPoint.sub(entityPosition).normalized();
            Vector3f backwardDirection = prevPoint.sub(entityPosition).normalized();

            // set the target direction the direction that is closest to the current velocity
            if (entityVelocity.sub(forwardDirection).length() < entityVelocity.sub(backwardDirection).length()) {
                targetDirection = forwardDirection;
            } else {
                targetDirection = backwardDirection;
            }
            // TODO: check this out
            targetDirection = forwardDirection;
        } else { // otherwise, just move towards the spline
            targetDirection = closestPoint.sub(entityPosition).normalized();
        }

        return targetDirection;
    }

    /**
     * Gets a copy of the closest spline point to the center of the given entity.
     * The center is the position + 0.5 * dimension
     *
     * @param position  the position of the entity
     * @param dimension the dimension of the entity
     * @return
     */
    private Vector3f getClosestSplinePoint(Vector3f position, Vector3f dimension) {
        return points[getClosestSplinePointIndex(position, dimension)];
    }

    /**
     * Gets the index of the closest spline point to the center of the given entity.
     * The center is the position + 0.5 * dimension
     *
     * @param position  the position of the entity
     * @param dimension the dimension of the entity
     * @return
     */
    private int getClosestSplinePointIndex(Vector3f position, Vector3f dimension) {
//        Vector3f center = position.add(dimension.scale(0.5f));
        Vector3f center = position;

        float minDistance = Float.MAX_VALUE;
        int closestIndex = 0;

        // try all different spline points
        for (int i = 0; i < points.length; i++) {
            // get the distance from the spline point to the center of the entity
            float curDist = center.sub(points[i]).length();


            if (curDist < minDistance) {
                minDistance = curDist;
                closestIndex = i;
            }
        }

        return closestIndex;
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
