package group4.ECS.entities.mobs;

import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.components.identities.JumpingWalkingMobComponent;
import group4.ECS.systems.movement.MovementHandlers.JumpingWalkingMobMovementHandler;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class ShootingJumpingWalkingMob extends Mob {
    /**
     * Creates a shooting jumping&walking mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public ShootingJumpingWalkingMob(Vector3f position, Level l) {
        super(position, l, JumpingWalkingMobMovementHandler.getInstance());
        this.add(new JumpingWalkingMobComponent());
    }

    public static String getName() {
        return "ShootingJumpingWalkingMob";
    }
}
