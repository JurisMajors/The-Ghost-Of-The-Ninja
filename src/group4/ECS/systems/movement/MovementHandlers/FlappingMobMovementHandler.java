package group4.ECS.systems.movement.MovementHandlers;

import group4.ECS.entities.mobs.FlappingMob;
import group4.maths.Vector3f;

public class FlappingMobMovementHandler extends AbstractMovementHandler<FlappingMob> {

    /**
     * Singleton
     **/
    private static AbstractMovementHandler me = new FlappingMobMovementHandler();

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        // flapping mobs can always jump
        return true;
    }

    public static AbstractMovementHandler getInstance() {
        return me;
    }
}
