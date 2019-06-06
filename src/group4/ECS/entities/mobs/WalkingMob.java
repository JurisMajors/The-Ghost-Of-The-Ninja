package group4.ECS.entities.mobs;

import group4.ECS.components.identities.WalkingMobComponent;
import group4.ECS.systems.movement.MovementHandlers.WalkingMobMovementHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class WalkingMob extends Mob {

    /**
     * Creates a walking mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public WalkingMob(Vector3f position, Level l) {
        super(position, l, WalkingMobMovementHandler.getInstance());
        this.add(new WalkingMobComponent());
    }

    public WalkingMob(Vector3f position, Level l, Texture texture, float[] texCoords) {
        super(position, l, texture, texCoords, WalkingMobMovementHandler.getInstance());
        this.add(new WalkingMobComponent());
    }

    public static String getName() {
        return "WalkingMob";
    }
}
