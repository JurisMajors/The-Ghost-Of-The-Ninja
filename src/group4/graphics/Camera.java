package group4.graphics;

import group4.game.Player;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

public class Camera {
    public Vector3f position;
    public Matrix4f view_matrix;
    private Player player;
    public Camera(Player player) {
        this.player = player;
        this.position = player.position;
        this.view_matrix = Matrix4f.translate(this.position);
    }

    void update() {
        this.position = player.position;
        this.view_matrix = Matrix4f.translate(this.position);
    }

    public Matrix4f getViewMatrix() {
        return this.view_matrix;
    }
}
