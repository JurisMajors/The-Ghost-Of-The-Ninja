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
        result.x = x * other.y - y * other.x;
        result.y = y * other.z - z * other.y;
        result.z = z * other.x - x * other.z;

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

    public float euclidDist(Vector3f other) {
        Vector3f diff = this.sub(other);
        return (float) Math.sqrt(diff.dot(diff));
    }

}
