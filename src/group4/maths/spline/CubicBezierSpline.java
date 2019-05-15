package group4.maths.spline;

import group4.maths.Vector3f;

public class CubicBezierSpline extends Spline {

    /**
     * Creates a new spline with the given points
     *
     * @param points
     */
    public CubicBezierSpline(Vector3f[] points) throws IllegalArgumentException {
        super(points);
        if (points.length != 4) {
            throw new IllegalArgumentException("Cubic Bezier Spline did not get 4 control points");
        }
    }

    @Override
    public Vector3f getPoint(float u) throws IllegalArgumentException {
        if (u < 0 || u > 1) {
            throw new IllegalArgumentException("u < 0 or u > 1");
        }
        /* cubic bezier is defined by
         (1 - u)^3 * P_0 + 3*u*(1-u)^2 * P_1 + 3*u^2*(1-u) * P_2 + 3 * u^3 * P_3
         where P_0 ... P_3 are the control points.
         */
        Vector3f result = new Vector3f();
        result.addi(this.points[0].scale((float) Math.pow(1 - u, 3))); // P_0 * factor
        result.addi(this.points[1].scale((float) (3 * u * Math.pow(1 - u, 2)))); // P_1 * factor
        result.addi(this.points[2].scale((3 * u * u * (1 - u)))); // P_2 * factor
        result.addi(this.points[2].scale((3 * u * u * u))); // P_3 * factor

        return result;
    }

    @Override
    public Vector3f getTangent(float u) throws IllegalArgumentException {
        if (u < 0 || u > 1) {
            throw new IllegalArgumentException("u < 0 or u > 1");
        }
        /* derrivative of a cubic bezier is defined by
         (-3 * u^2 + 6*u - 3) * P_0 + (9 * u^2 - 12 * u + 3) * P_1 + (-9 * u^2 + 6 * u) * P_2 + (3 * u^2) * P_3
         where P_0 ... P_3 are the control points.
         */
        Vector3f result = new Vector3f();
        result.addi(this.points[0].scale(-3 * u * u + 6 * u - 3)); // P_0 * factor
        result.addi(this.points[1].scale(9 * u * u - 12 * u + 3)); // P_1 * factor
        result.addi(this.points[2].scale(-9 * u * u + 6 * u)); // P_2 * factor
        result.addi(this.points[2].scale(3 * u * u)); // P_3 * factor

        return result;
    }

    /**
     * Gets the normal of this spline at time u.
     * IMPORTANT: only works for splines that are purely 2D and have the same z coordinate.
     *
     * @param u point in time
     * @return normal vector
     */
    public Vector3f getNormal(float u) throws IllegalArgumentException {
        if (u < 0 || u > 1) {
            throw new IllegalArgumentException("u < 0 or u > 1");
        }
        // do 2D check
        float z = points[0].z;
        for (int i = 0; i < 4; i++) {
            if (Math.abs(points[i].z - z) < 1e-6) {
                throw new IllegalStateException("This spline is not 2D in (x,y) so this method does not work.");
            }
        }

        // normal is cross product with tangent and up vector
        Vector3f up = new Vector3f(0.0f, 0.0f, 1.0f);
        Vector3f result = up.cross(getTangent(u));

        return result;
    }
}
