package group4.maths;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Matrix4f {

    // length and width of the matrix
    private static int SIZE = 4;

    public float[] elements;

    /**
     * Creates a zero matrix
     */
    public Matrix4f() {
        elements = new float[SIZE * SIZE];
    }

    /**
     * Creates 4D identity matrix.
     *
     * @return identity matrix
     */
    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();

        // set the diagonals to 1
        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    /**
     * Creates 2D rotation matrix over given angle.
     *
     * @param angle in degrees.
     * @return rotation matrix
     */
    public static Matrix4f rotate2D(float angle) {
        Matrix4f result = identity();

        float radians = (float) Math.toRadians(angle);

        // cache sin(angle) and cos(angle) for more speed
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);

        result.elements[0 + 0 * 4] = cos;
        result.elements[0 + 1 * 4] = sin;
        result.elements[1 + 0 * 4] = -sin;
        result.elements[1 + 1 * 4] = cos;

        return result;
    }

    /**
     * Create translation matrix based on vector
     *
     * @param vector
     * @return translation matrix
     */
    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = identity();

        result.elements[0 + 3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;

        return result;
    }

    /**
     * Creates a scaling matrix that scales the x y and z coordinates according to the x y z coordinates in vector.
     *
     * @param vector
     * @return scaling matrix
     */
    public static Matrix4f scale(Vector3f vector) {
        Matrix4f result = identity();

        result.elements[0 + 0 * 4] = vector.x;
        result.elements[1 + 1 * 4] = vector.y;
        result.elements[2 + 2 * 4] = vector.y;

        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) throws NotImplementedException {
        throw new NotImplementedException();
    }

}
