package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class NonGravityMobMovementSystem extends MobMovementSystem {

    public NonGravityMobMovementSystem() {
        super(Families.movingNonGravityMobFamily);
    }

    @Override
    protected void doGravity(Entity e, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        PositionComponent ppc = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));

        //accelerate on the y coordinate towards player
        if (pc.position.y < ppc.position.y) {
            mc.velocity.y = Math.min(mc.velocityRange.y, mc.velocity.y + mc.acceleration.y);
        } else if (pc.position.y > ppc.position.y) {
            mc.velocity.y = Math.max(-mc.velocityRange.y, mc.velocity.y - mc.acceleration.y);
        } else if (mc.velocity.y != 0) { //if entity has same y coordinate as player decrease (absolute value of) velocity.y
            mc.velocity.y -= mc.velocity.y / Math.abs(mc.velocity.y) * mc.acceleration.y;
        }
    }
}
