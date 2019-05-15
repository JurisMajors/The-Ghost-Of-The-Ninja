package group4.ECS.systems;

import group4.ECS.etc.Families;
import group4.maths.Vector3f;

public class WalkingMobMovementSystem extends MobMovementSystem {

    public WalkingMobMovementSystem() {
        super(Families.walkingMobFamily);
    }

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        return false;
    }
}
