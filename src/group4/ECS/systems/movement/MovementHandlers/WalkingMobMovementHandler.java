package group4.ECS.systems.movement.MovementHandlers;

import group4.ECS.entities.mobs.WalkingMob;
import group4.maths.Vector3f;

public class WalkingMobMovementHandler extends AbstractMovementHandler<WalkingMob> {

    /**
     * Singleton
     **/
    private static AbstractMovementHandler me = new WalkingMobMovementHandler();

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        // walking mobs can never jump
        return false;
    }

    public static AbstractMovementHandler getInstance() {
        return me;
    }
}
