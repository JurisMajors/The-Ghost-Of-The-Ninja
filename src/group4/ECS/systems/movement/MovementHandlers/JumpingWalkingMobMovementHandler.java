package group4.ECS.systems.movement.MovementHandlers;

import group4.ECS.entities.mobs.JumpingWalkingMob;

public class JumpingWalkingMobMovementHandler extends AbstractMovementHandler<JumpingWalkingMob> {

    /**
     * Singleton
     **/
    private static AbstractMovementHandler me = new JumpingWalkingMobMovementHandler();


    public static AbstractMovementHandler getInstance() {
        return me;
    }
}
