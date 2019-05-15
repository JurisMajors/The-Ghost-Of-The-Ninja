package group4.ECS.systems;

import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.etc.Families;
import group4.maths.Vector3f;

public class FlyingMobMovementSystem extends MobMovementSystem {

    public FlyingMobMovementSystem() {
        super(Families.flyingMobFamily);
    }

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        return true;
    }

    @Override
    protected boolean canMoveDown() {
        super.canMoveDown();
        return true;
    }

    @Override
    protected void doGravity(MovementComponent mc, GravityComponent gc) {
        super.doGravity(mc, gc);
        return;
    }
}
