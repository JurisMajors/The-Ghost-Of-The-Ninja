package group4.ECS.entities.mobs;

import group4.ECS.components.*;
import group4.ECS.components.identities.JumpingWalkingMobComponent;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class ShootingJumpingWalkingMob extends Mob {
    /**
     * Creates a shooting jumping&walking mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public ShootingJumpingWalkingMob(Vector3f position, Level l) {
        super(position, l);
        this.add(new ShootingComponent(new Vector3f(0.5f, 2.0f, 0.0f), 150));
        this.add(new JumpingWalkingMobComponent());
    }
}
