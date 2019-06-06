package group4.ECS.entities.mobs;

import group4.ECS.components.identities.JumpingMobComponent;
import group4.ECS.systems.movement.MovementHandlers.JumpingMobMovementHandler;
import group4.ECS.systems.movement.MovementHandlers.JumpingWalkingMobMovementHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class JumpingMob extends Mob {

    /**
     * Creates a jumping mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public JumpingMob(Vector3f position, Level l) {
        super(position, l, JumpingMobMovementHandler.getInstance());
        this.add(new JumpingMobComponent());
    }

    public JumpingMob(Vector3f position, Level l, Texture tex, float[] texCoords) {
        super(position, l, tex, texCoords, JumpingWalkingMobMovementHandler.getInstance());
        this.add(new JumpingMobComponent());
    }

    public static String getName() {
        return "JumpingMob";
    }
}
