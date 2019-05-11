package group4.ECS.components;

import com.badlogic.ashley.core.Component;

/**
 * TODO: gravity can be decoupled, need for both mass and weight?
 */
public class PhysicsComponent implements Component {

    public float mass = 0.0f;
    public float weight = 0.0f;
    public float gravity = 9.81f;   // could also be 0.0f by default, using real values might be handy to use

    public PhysicsComponent(float mass, float weight) {
        this.mass = mass;
        this.weight = weight;
    }

}
