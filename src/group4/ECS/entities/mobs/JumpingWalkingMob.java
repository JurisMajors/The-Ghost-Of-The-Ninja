package group4.ECS.entities.mobs;

import group4.ECS.components.identities.JumpingWalkingMobComponent;
import group4.ECS.systems.movement.MovementHandlers.JumpingWalkingMobMovementHandler;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class JumpingWalkingMob extends Mob {

    /**
     * Creates a jumping&walking mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public JumpingWalkingMob(Vector3f position, Level l) {
        super(position, l, JumpingWalkingMobMovementHandler.getInstance());
        this.add(new JumpingWalkingMobComponent());
    }

    public static String getName() {
        return "JumpingWalkingMob";
    }
}
