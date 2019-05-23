package group4.ECS.systems.movement;

import group4.ECS.etc.Families;
import group4.maths.Vector3f;

public class WalkingMobMovementSystem extends MobMovementSystem {

    public WalkingMobMovementSystem() {
        super();
    }

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        // walking mobs can never jump
        return false;
    }
}
