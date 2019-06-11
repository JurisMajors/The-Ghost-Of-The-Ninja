package group4.ECS.systems.GraphHandlers;

import group4.ECS.entities.AStarMobs.JumpingWalkingAStarMob;
import group4.ECS.entities.mobs.JumpingWalkingMob;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;

public class JumpingWalkingAStarMobGraphHandler extends AbstractGraphHandler<JumpingWalkingAStarMob> {
    private static AbstractGraphHandler agh = new JumpingWalkingAStarMobGraphHandler();
    public static AbstractGraphHandler getInstance() {
        return agh;
    }
}
