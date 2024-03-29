package group4.ECS.systems.movement.MovementHandlers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.mobs.FlyingMob;
import group4.ECS.etc.Families;
import group4.maths.Vector3f;

public class FlyingMobMovementHandler extends AbstractMovementHandler<FlyingMob> {

    /**
     * Singleton
     **/
    private static AbstractMovementHandler me = new FlyingMobMovementHandler(Families.flyingMobFamily, 2);

    public FlyingMobMovementHandler(Family family, int priority) {
        super(family, priority);
    }

    @Override
    protected boolean canJump(Vector3f velocity) {
        // flying mobs can always jump
        return true;
    }

    @Override
    protected boolean canMoveDown() {
        // flying mobs can always move down
        return true;
    }

    @Override
    protected void becomeIdle(MovementComponent mc) {
        mc.velocity.x = 0;
        mc.velocity.y = 0;
    }

    @Override
    protected void doGravity(MovementComponent mc, GravityComponent gc) {
        // flying mobs are not affected by gravity
    }

    public static AbstractMovementHandler getInstance() {
        return me;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // DONT DO ANYTHING HERE
    }
}
