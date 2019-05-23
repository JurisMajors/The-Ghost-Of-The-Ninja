package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.identities.CameraComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.game.Main;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

public class Camera extends Entity {

    public Camera() {
        // add needed components
        this.add(new PositionComponent(new Vector3f(0.0f, 0.0f, 0.0f)));

        // Centered projection matrix
        this.add(new CameraComponent(Matrix4f.orthographic(-Main.SCREEN_WIDTH / 2, Main.SCREEN_WIDTH / 2, - Main.SCREEN_HEIGHT / 2, Main.SCREEN_HEIGHT / 2, -1.0f, 1.0f)));
    }

    public String getName() {
        return "Camera";
    }
}
