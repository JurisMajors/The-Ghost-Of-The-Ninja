package group4.maths;

public class Vector3f {

    public float x, y, z;

    /**
     * Default constructor creates vector (0,0,0)
     */
    public Vector3f() {
        x = y = z = 0.0f;
    }

    /**
     * Creates vector (x,y,z)
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a copy of another vector
     * @param toCopy the vector to copy
     */
    public Vector3f(Vector3f toCopy) {
        this.x = toCopy.x;
        this.y = toCopy.y;
        this.z = toCopy.z;
    }

    /**
     * Adds this vector to another vector and results the result.
     *
     * @param other vector
     * @return returns this + other
     */
    public Vector3f add(Vector3f other) {
        return new Vector3f(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Adds other vector to this vector and stores the result in this vector.
     *
     * @param other vector
     */
    public void addi(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    /**
     * Subtracts other from this and returns the result.
     *
     * @param other vector
     * @return this - other
     */
    public Vector3f sub(Vector3f other) {
        return new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Subtracts other from this inplace.
     *
     * @param other vector
     */
    public void subi(Vector3f other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    /**
     * Scales the current vector by d and returns the result.
     *
     * @param d float
     * @return this * d
     */
    public Vector3f scale(float d) {
        return new Vector3f(this.x * d, this.y * d, this.z * d);
    }

    /**
     * Scales the current vector by d inplace.
     *
     * @param d float
     */
    public void scalei(float d) {
        this.x *= d;
        this.y *= d;
        this.x *= d;
    }

    /**
     * Computes and returns the dot product of this vector with other
     *
     * @param other vector
     * @return this * other
     */
    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Computes and returns the cross producto of this vector with other.
     *
     * @param other vector
     * @return cross product
     */
    public Vector3f cross(Vector3f other) {
        Vector3f result = new Vector3f();
        result.x = y * other.z - z * other.y;
        result.y = z * other.x - x * other.z;
        result.z = x * other.y - y * other.x;

        return result;
    }

    /**
     * Returns the 2 norm length of the vector.
     *
     * @return length
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns a normalized version of this vector.
     *
     * @return vector.
     */
    public Vector3f normalized() {
        Vector3f result = new Vector3f();
        float len = length();
        result.x = x / len;
        result.y = y / len;
        result.z = z / len;

        return result;
    }

    /**
     * Normalizes this vector to have lenght 1.
     */
    public void normalizei() {
        float len = length();
        this.x /= len;
        this.y /= len;
        this.z /= len;
    }

    /**
     * Calculates the euclidean distance between two vectors
     * @param other the other vector to calculate the distance to
     * @return euclid distance between this and other
     */
    public float euclidDist(Vector3f other) {
        Vector3f diff = this.sub(other);
        return (float) Math.sqrt(diff.dot(diff));
    }

    /**
     * Rotates the vector in place according to the angle in the XY place
     * @param angle the angle in degrees to rotate over in the XY plane
     */
    public void rotateXYi(float angle) {
        float[] newCoords = this.getRotatedXY(angle);
        this.x = newCoords[0];
        this.y = newCoords[1];
    }

    /**
     * Rotates the vector according to the angle in the XY place
     * @param angle the angle in degrees to rotate over in the XY plane
     * @return this, but rotated over the angle
     */
    public Vector3f rotateXY(float angle) {
        float[] newCoords = this.getRotatedXY(angle);
        return new Vector3f(newCoords[0], newCoords[1], this.z);
    }

    private float[] getRotatedXY(float angle) {
        angle = (float) Math.toRadians(angle);
        float newX = (float) (this.x * Math.cos(angle) - this.y * Math.sin(angle));
        float newY = (float) (this.x * Math.sin(angle) + this.y * Math.cos(angle));
        return new float[]{newX, newY};
    }

    @Override
    public String toString() {
        return String.format("{%f, %f, %f}", this.x, this.y, this.z);
    }

    /**
     * Returns a vector with absolute value for x,y,z
     * @return vector
     */
    public Vector3f abs() {
        return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    /**
     * Returns a copy of the vector with the smallest length amongst a and b.
     * @param a vector
     * @param b vector
     * @return copy of smallest vector of a and b
     */
    public static Vector3f min(Vector3f a, Vector3f b) {
        double lenA = a.length();
        double lenB = b.length();
        if (lenA <= lenB) {
            return new Vector3f(a);
        }

        return new Vector3f(b);
    }

}
