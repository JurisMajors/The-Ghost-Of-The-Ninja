package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class MovementComponent implements Component {

    public Vector3f velocity;
    public Vector3f velocityRange;
    public Vector3f acceleration;

    // joris TODO: subclasses

    /**
     * default constructor (standing)
     *
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and (-velocityRange.y<=)velocity.y<=velocityRange.y
     */
    public MovementComponent(Vector3f velocityRange) {
        this.velocity = new Vector3f();
        this.velocityRange = velocityRange;
        this.acceleration = new Vector3f();
    }

    /**
     * Creates a movement component with no acceleration (zero vector)
     *
     * @param velocity      velocity vector of mob
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and (-velocityRange.y<=)velocity.y<=velocityRange.y
     */
    public MovementComponent(Vector3f velocity, Vector3f velocityRange) {
        this.velocity = velocity;
        this.velocityRange = velocityRange;
        this.acceleration = new Vector3f(); //no acceleration (zero vector)
    }

    /**
     * Creates a movement component
     *
     * @param velocity      velocity vector
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and (-velocityRange.y<=)velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector
     */
    public MovementComponent(Vector3f velocity, Vector3f velocityRange, Vector3f acceleration) {
        this.velocity = velocity;
        this.velocityRange = velocityRange;
        this.acceleration = acceleration;
    }

}
