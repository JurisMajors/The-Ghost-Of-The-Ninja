package group4.graphics;

import group4.maths.Matrix4f;
import group4.maths.Vector3f;

public class Camera {
    public Vector3f position;
    public Matrix4f view_matrix;

    public Camera() {
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.view_matrix = Matrix4f.translate(this.position);
    }

    public void update() {
        this.view_matrix = Matrix4f.translate(this.position);
    }

    public Matrix4f getViewMatrix() {
        return this.view_matrix;
    }
}
