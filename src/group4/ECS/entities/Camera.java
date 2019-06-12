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
        this.add(new PositionComponent(new Vector3f()));

        // Centered projection matrix
        this.add(new CameraComponent());
    }

    public static String getName() {
        return "Camera";
    }
}
