package group4.maths.spline;

import group4.maths.Vector3f;

public class MultiSpline {

    private Spline[] splines;
    private int numSplines;

    /**
     * Creates a multispline, a list of splines concatinated.
     * @param points control points for the splines in order.
     * @throws IllegalArgumentException if the number of input points is not a multiple of 4 (currently works for cubic
     * bezier splines)
     */
    public MultiSpline(Vector3f[] points) throws IllegalArgumentException {
        if (points.length % 4 != 0) {
            throw new IllegalArgumentException("Number of input points should be a multiple of 4");
        }

        this.numSplines = points.length / 4;
        this.splines = new Spline[numSplines];

        // create the splines
        for (int i = 0; i < numSplines; i++) {
            Vector3f[] splinePoints = new Vector3f[4];
            for (int j = 0; j < 4; j++) {
                splinePoints[j] = points[i * 4 + j];
            }

            splines[i] = new CubicBezierSpline(splinePoints);
        }
    }

    /**
     * Gets the point on this multi spline at time u
     *
     * @param u float between 0.0f and 1.0f
     * @return point vector
     */
    public Vector3f getPoint(float u) throws IllegalArgumentException {
        if (u < 0 || u > 1) {
            throw new IllegalArgumentException("u < 0 or u > 1");
        }

        // get the spline we are currently on
        int i = getSplineIndex(u);
        float time = getScaledTime(u);

        return splines[i].getPoint(time);
    }

     /**
     * Gets the tangent on this multi spline at time u
     *
     * @param u float between 0.0f and 1.0f
     * @return tangent vector
     */
    public Vector3f getTangent(float u) throws IllegalArgumentException {
        if (u < 0 || u > 1) {
            throw new IllegalArgumentException("u < 0 or u > 1");
        }

        int i = getSplineIndex(u);
        float time = getScaledTime(u);

        return splines[i].getTangent(time);
    }

     /**
     * Gets the normal on this multi spline at time u
     *
     * @param u float between 0.0f and 1.0f
     * @return normal vector
     */
    public Vector3f getNormal(float u) throws IllegalArgumentException {
        if (u < 0 || u > 1) {
            throw new IllegalArgumentException("u < 0 or u > 1");
        }

        int i = getSplineIndex(u);
        float time = getScaledTime(u);

        return splines[i].getNormal(time);
    }

    /**
     * Given a float u, which represents a time between 0.0f and 1.0f,
     * returns the index of the splines for this time.
     *
     * @param u time
     * @return index of spline in splines array
     */
    private int getSplineIndex(float u) {
        int idx = (int) (u * numSplines);
        // for u=1.0 we would get numSplines which is out of range, so for that specific case we cap it here
        idx = Math.min(idx, numSplines - 1);
        return idx;
    }

    /**
     * Given a global time u between 0.0f and 1.0f, find the local time on the spline for this time.
     * @param u float
     * @return local time between 0.0f and 1.0f
     */
    private float getScaledTime(float u) {
        int i = getSplineIndex(u);

        // turn u into the range 0.0f till 1/numSplines
        float shifted = u - (i / (float) numSplines);
        // scale to [0.0f, 1.0f]
        float scaled = shifted * numSplines;

        return scaled;
    }

}
