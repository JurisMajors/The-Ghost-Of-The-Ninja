package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.entities.HierarchicalPlayer;
import group4.game.Main;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

public class CameraComponent implements Component {
    public Matrix4f projectionMatrix;
    public Matrix4f projectionMatrixHorizontalFlip;
    public Matrix4f viewMatrix;

    public CameraComponent() {
        this.projectionMatrix = Matrix4f.orthographic(-Main.SCREEN_WIDTH / 2, Main.SCREEN_WIDTH / 2,
                - Main.SCREEN_HEIGHT / 2, Main.SCREEN_HEIGHT / 2, -1.0f, 1.0f); // Initialize the projectionMatrix once (for now)

        this.projectionMatrixHorizontalFlip = Matrix4f.orthographic(Main.SCREEN_WIDTH / 2, -Main.SCREEN_WIDTH / 2,
                - Main.SCREEN_HEIGHT / 2, Main.SCREEN_HEIGHT / 2, -1.0f, 1.0f);
        // No need to set the viewMatrix as we update it every frame.
    }
}
