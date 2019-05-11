package group4.ECS.entities.mobs;

import group4.ECS.components.GravityComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class WalkingMob extends Mob {

    /**
     * Creates a walking mob (a mob affected by gravity)
     *
     * @param position      left-bottom-back corner of the cuboid representing the mob
     * @param dimension     such that the right-top-front corner of the cuboid representing the mob is position+dimension
     * @param velocity      velocity vector of mob
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and -velocityRange.y<=velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector of mob
     * @param gravity       acceleration vector of mob due to gravity
     */
    public WalkingMob(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, Vector3f gravity, Shader shader, Texture texture) {
        // setup basic mob and add it to the engine
        super(position, dimension, velocity, velocityRange, acceleration, shader, texture);

        // add gravity to the mob
        this.add(new GravityComponent(gravity));
    }
}
