package group4.ECS.entities.world;

import group4.ECS.components.RangeComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;


public class MovingPlatform extends Platform {

    /**
     * Creates a moving platform
     *
     * @param position       left-bottom-back corner of the cuboid representing the platform
     * @param dimension      such that the right-top-front corner of the cuboid representing the platform is position+dimension
     * @param velocity       velocity vector of platform
     * @param lbbCornerRange left-bottom-back corner of the cuboid representing the accessible range of the map for the platform
     * @param rtfCornerRange right-top-front corner of the cuboid representing the accessible range of the map for the platform
     * @param shader         shader for this platform
     * @param texture        texture for this platform
     */
    public MovingPlatform(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f lbbCornerRange, Vector3f rtfCornerRange, Shader shader, Texture texture) {
        // create position, dimension and graphics components and adds to engine
        super(position, dimension, shader, texture);

        //velocity of platforms could only be equal to velocity or -velocity thus velocityRange=|velocity|
        this.add(new MovementComponent(velocity, velocity.abs()));
        //range of the lbb corner thus the rtf corner of the range is rtfCornerRange-dimension
        this.add(new RangeComponent(lbbCornerRange, rtfCornerRange.sub(dimension)));
    }

    public static String getName() {
        return "MovingPlatform";
    }
}
