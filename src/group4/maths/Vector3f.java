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
     * @param x
     * @param y
     * @param z
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
