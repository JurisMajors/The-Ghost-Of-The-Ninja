package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class Mob extends Entity {

    // joris TODO: graphics

    /**
     * Creates a basic mob with a given position, dimension, velocity, velocityRange, acceleration.
     *
     * @param position      left-bottom-back corner of the cuboid representing the mob
     * @param dimension     such that the right-top-front corner of the cuboid representing the mob is position+dimension
     * @param velocity      velocity vector of mob
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and -velocityRange.y<=velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector of mob
     */
    public Mob(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new MobComponent());
        TheEngine.getInstance().addEntity(this);
    }

    /**
     * Creates most basic mob, with a given position and dimension.
     * Initial velocity, velocityRange and acceleration is set to 0.
     *
     * @param position  left-bottom-back corner of the cuboid representing the mob
     * @param dimension such that the right-top-front corner of the cuboid representing the mob is position+dimension
     */
    public Mob(Vector3f position, Vector3f dimension) {
        // call other constructor with velocity, velocityRange, acceleration = 0
        this(position, dimension, new Vector3f(), new Vector3f(), new Vector3f());
    }

}
