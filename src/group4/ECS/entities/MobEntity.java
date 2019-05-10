package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class MobEntity extends Entity {

    public MobEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration){
        this.add(new PositionComponent(position, dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new MobComponent());
        TheEngine.getInstance().addEntity(this);
    }

    public MobEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, Vector3f gravity){
        this.add(new PositionComponent(position, dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new GravityComponent(gravity));
        this.add(new MobComponent());
        TheEngine.getInstance().addEntity(this);
    }

}
