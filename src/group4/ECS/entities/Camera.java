package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.CameraComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class Camera extends Entity {
    public Camera() {
        // add needed components
        this.add(new PositionComponent(new Vector3f(0.0f, 0.0f, 0.0f)));
        this.add(new CameraComponent());

        // register to engine
        TheEngine.getInstance().addEntity(this);
    }
}
