package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Matrix4f;

public class CameraComponent implements Component {
    public Matrix4f projectionMatrix;
    public Matrix4f viewMatrix;

    public CameraComponent(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix; // Initialize the projectionMatrix once (for now)
        // No need to set the viewMatrix as we update it every frame.
    }
}
