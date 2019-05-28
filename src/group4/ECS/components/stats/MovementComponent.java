package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import freemarker.template.utility.RichObjectWrapper;
import group4.maths.Vector3f;

public class MovementComponent implements Component {

    /* constants for orientation */
    public static int LEFT = -1;
    public static int RIGHT = 1;

    public Vector3f velocity;
    public Vector3f velocityRange;
    public Vector3f acceleration;
    public int orientation;

    // joris TODO: subclasses

    /**
     * default constructor (standing)
     *
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and (-velocityRange.y<=)velocity.y<=velocityRange.y
     */
    public MovementComponent(Vector3f velocityRange) {
        // right by default
        this.orientation = RIGHT;
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
        // right by default
        this.orientation = RIGHT;
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
        // right by default
        this.orientation = RIGHT;
        this.velocity = velocity;
        this.velocityRange = velocityRange;
        this.acceleration = acceleration;
    }

    public MovementComponent(Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, int dimension) {
        // right by default
        this.orientation = dimension;
        this.velocity = velocity;
        this.velocityRange = velocityRange;
        this.acceleration = acceleration;
    }

}
