package group4.ECS.systems.movement;

import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Families;
import group4.maths.Vector3f;

public class FlyingMobMovementSystem extends MobMovementSystem {

    public FlyingMobMovementSystem() {
        super(Families.flyingMobFamily);
    }

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        // flying mobs can always jump
        return true;
    }

    @Override
    protected boolean canMoveDown() {
        super.canMoveDown();
        // flying mobs can always move down
        return true;
    }

    @Override
    protected void doGravity(MovementComponent mc, GravityComponent gc) {
        super.doGravity(mc, gc);
        // flying mobs are not affected by gravity
        return;
    }
}
