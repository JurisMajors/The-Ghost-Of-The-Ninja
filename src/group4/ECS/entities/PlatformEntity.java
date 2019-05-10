package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class PlatformEntity extends Entity {

    public PlatformEntity(Vector3f position, Vector3f dimension){
        this.add(new PositionComponent(position, dimension));
        this.add(new PlatformComponent());
        TheEngine.getInstance().addEntity(this);
    }

    public PlatformEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f lbbCornerRange, Vector3f rtfCornerRange){
        this.add(new PositionComponent(position, dimension));
        this.add(new MovementComponent(velocity, velocity));
        this.add(new RangeComponent(lbbCornerRange.add(dimension), rtfCornerRange.add(dimension.scale(-1.0f)))); //range of center
        this.add(new PlatformComponent());
        TheEngine.getInstance().addEntity(this);
    }

}
