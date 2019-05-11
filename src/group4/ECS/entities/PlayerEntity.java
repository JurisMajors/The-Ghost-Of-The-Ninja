package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class PlayerEntity extends Entity {

    // joris TODO: graphicsComponent

    /**
     * Creates a player
     *
     * @param position      center point of player
     * @param dimension     along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention
     * @param velocity      velocity vector of player
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector of player
     * @param gravity       acceleration vector of player due to gravity
     */
    public PlayerEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, Vector3f gravity) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new GravityComponent(gravity));
        this.add(new PlayerComponent());
        TheEngine.getInstance().addEntity(this);
    }

}
