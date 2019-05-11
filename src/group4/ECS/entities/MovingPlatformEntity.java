package group4.ECS.entities;

import group4.ECS.components.MovementComponent;
import group4.ECS.components.PlatformComponent;
import group4.ECS.components.RangeComponent;
import group4.maths.Vector3f;


public class MovingPlatformEntity extends PlatformEntity {


    /**
     * Creates a moving platform
     *
     * @param position       center point of platform
     * @param dimension      along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention
     * @param texturePath    path to the texture to be used for this platform
     * @param velocity       velocity vector of platform
     * @param lbbCornerRange left-bottom-back corner of the cuboid representing the accessible range of the map for the platform
     * @param rtfCornerRange right-top-front corner of the cuboid representing the accessible range of the map for the platform
     */
    public MovingPlatformEntity(Vector3f position, Vector3f dimension, String texturePath, Vector3f velocity, Vector3f lbbCornerRange, Vector3f rtfCornerRange) {
        super(position, dimension, texturePath);

        // this should be added in parent constructor
//        this.add(new PositionComponent(position, dimension));
//        this.add(new GraphicsComponent("src/group4/res/shaders/simple/", "src/group4/res/textures/brick.png", va, ic, tc));

        //velocity of platform could only be equal to velocity or -velocity thus velocityRange=|velocity|
        this.add(new MovementComponent(velocity, velocity.abs()));
        //range of the center point thus lbb corner is lbbCornerRange+dimension and rtf corner is rtfCornerRange-dimension
        this.add(new RangeComponent(lbbCornerRange.add(dimension), rtfCornerRange.add(dimension.scale(-1.0f))));
        this.add(new PlatformComponent());

        // TODO: can this be called before adding components?
        // currently this is being done in the parent constructor
//        TheEngine.getInstance().addEntity(this);
    }


}
