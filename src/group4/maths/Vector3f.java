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
     * @param other vector
     * @return returns this + other
     */
    public Vector3f add(Vector3f other) {
        return new Vector3f(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Adds other vector to this vector and stores the result in this vector.
     * @param other vector
     */
    public void addi(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    /**
     * Subtracts other
     * @param other
     * @return
     */
    public Vector3f sub(Vector3f other) {
        return new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3f scale(float d) {
        return new Vector3f(this.x*d,this.y*d,this.z*d);
    }
}
