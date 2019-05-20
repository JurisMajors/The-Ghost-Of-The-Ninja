package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;

public class FlappingMobMovementSystem extends JumpingMobMovementSystem {

    public FlappingMobMovementSystem() {
        super(Families.flappingMobFamily);
    }

    @Override
    protected boolean canJump(Vector3f velocity) {
        super.canJump(velocity);
        // flapping mobs can always jump
        return true;
    }
}
