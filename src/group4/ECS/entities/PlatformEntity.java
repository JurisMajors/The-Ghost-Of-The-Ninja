package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class PlatformEntity extends Entity {

    /**
     * Creates a static platform
     *
     * @param position  center point of platform
     * @param dimension along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention
     */
    public PlatformEntity(Vector3f position, Vector3f dimension) {
        this.add(new PositionComponent(position, dimension));
        this.add(new PlatformComponent());
        TheEngine.getInstance().addEntity(this);
    }

    /**
     * Creates a moving platform
     *
     * @param position       center point of platform
     * @param dimension      along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention
     * @param velocity       velocity vector of platform
     * @param lbbCornerRange left-bottom-back corner of the cuboid representing the accessible range of the map for the platform
     * @param rtfCornerRange right-top-front corner of the cuboid representing the accessible range of the map for the platform
     */
    public PlatformEntity(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f lbbCornerRange, Vector3f rtfCornerRange) {
        this.add(new PositionComponent(position, dimension));
        //velocity of platforms could only be equal to velocity or -velocity thus velocityRange=|velocity|
        this.add(new MovementComponent(velocity, velocity.abs()));
        //range of the center point thus lbb corner is lbbCornerRange+dimension and rtf corner is rtfCornerRange-dimension
        this.add(new RangeComponent(lbbCornerRange.add(dimension), rtfCornerRange.add(dimension.scale(-1.0f))));
        this.add(new PlatformComponent());
        TheEngine.getInstance().addEntity(this);
    }

}
