package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class MovementComponent implements Component {
    public Vector3f velocity;
    public Vector3f acceleration;

    public MovementComponent(Vector3f velocity, Vector3f acceleration) {
        this.velocity = velocity;
        this.acceleration = acceleration;
    }
}
