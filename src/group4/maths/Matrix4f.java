package group4.maths;

import group4.utils.BufferUtils;

import java.nio.FloatBuffer;

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

    /**
     * Creates an orthographic view matrix translating world space to view space.
     *
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     * @return orthographic matrix
     */
    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = identity();

        // squash the screen
        result.elements[0 + 0 * 4] = 2.0f / (right - left);
        result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
        result.elements[2 + 2 * 4] = 2.0f / (far - near);

        // translate the screen
        result.elements[0 + 3 * 4] = -1 * (right + left) / (right - left);
        result.elements[1 + 3 * 4] = -1 * (top + bottom) / (top - bottom);
        result.elements[2 + 3 * 4] = -1 * (far + near) / (far - near);

        return result;
    }

    /**
     * Multiplies the current matrix with other. Returns the result.
     *
     * @param other
     * @return this * other
     */
    public Matrix4f multiply(Matrix4f other) {
        Matrix4f result = new Matrix4f();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                float sum = 0.0f;
                for (int elem = 0; elem < SIZE; elem++) {
                    sum += elements[row + elem * 4] * other.elements[elem * 4 + col];
                }
                result.elements[row + col * 4] = sum;
            }
        }

        return result;
    }

    /**
     * Adds the current matrix to other. Returns the result.
     *
     * @param other
     * @return this * other
     */
    public Matrix4f add(Matrix4f other) {
        Matrix4f result = new Matrix4f();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                result.elements[row + col * 4] = elements[row + col * 4] + other.elements[row + col * 4];
            }
        }

        return result;
    }

    /**
     * Create a float buffer from the elements in this matrix
     *
     * @return float buffer
     */
    public FloatBuffer toFloatBuffer() {
        return BufferUtils.getFloatBuffer(elements);
    }

    /**
     * Returns the translational column from the matrix, assuming the matrix we use it
     * on defines homogeneous coordinates.
     *
     * @return Vector3f, the translation as defined by the transformation matrix.
     */
    public Vector3f getTranslation() {
        return new Vector3f(this.elements[12], this.elements[13], this.elements[14]); // We don't take [15] as we assume it is always 1
    }
}
