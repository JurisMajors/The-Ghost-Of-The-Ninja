package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class MobEntity extends Entity {

    // joris TODO: graphics

    /**
     * Creates a flying mob (not affected by gravity)
     *
     * @param position      left-bottom-back corner of the cuboid representing the mob
     * @param dimension     such that the right-top-front corner of the cuboid representing the mob is position+dimension
     * @param velocity      velocity vector of mob
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and -velocityRange.y<=velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector of mob
     */
    public MobEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration) {
        this.add(new PositionComponent(position, dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new MobComponent());
        TheEngine.getInstance().addEntity(this);
    }

    /**
     * Creates a walking and jumping mob (no jumping if velocityRange.y=0)
     *
     * @param position      left-bottom-back corner of the cuboid representing the mob
     * @param dimension     such that the right-top-front corner of the cuboid representing the mob is position+dimension
     * @param velocity      velocity vector of mob
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and -velocityRange.y<=velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector of mob
     * @param gravity       acceleration vector of mob due to gravity
     */
    public MobEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, Vector3f gravity) {
        this.add(new PositionComponent(position, dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new GravityComponent(gravity));
        this.add(new MobComponent());
        TheEngine.getInstance().addEntity(this);
    }

}
