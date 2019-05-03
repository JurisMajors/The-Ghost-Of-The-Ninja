package group4.maths;

public class Vector3i {

    public int x, y, z;

    /**
     * Creates vector (0,0,0)
     */
    public Vector3i() {
        x = y = z = 0;
    }

    /**
     * Creates vector (x,y,z)
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
