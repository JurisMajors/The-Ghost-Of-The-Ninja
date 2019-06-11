package group4.ECS.systems.GraphHandlers;

import group4.ECS.entities.AStarMobs.WalkingAStarMob;
import group4.ECS.entities.mobs.WalkingMob;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.maths.Vector3f;

public class WalkingAStarMobGraphHandler extends AbstractGraphHandler<WalkingAStarMob> {
    private static AbstractGraphHandler agh = new JumpingWalkingAStarMobGraphHandler();
    public static AbstractGraphHandler getInstance() {
        return agh;
    }
}
