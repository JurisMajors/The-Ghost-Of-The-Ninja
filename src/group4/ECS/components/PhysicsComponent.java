package group4.ECS.components;

import com.badlogic.ashley.core.Component;

public class PhysicsComponent implements Component {

    public float mass = 0.0f;
    public float weight = 0.0f;
    public float gravity = 9.81f;   // could also be 0.0f by default
    public boolean collidable = false;

}
