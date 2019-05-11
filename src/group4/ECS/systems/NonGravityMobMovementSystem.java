package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class NonGravityMobMovementSystem extends IteratingSystem {

    public NonGravityMobMovementSystem() {
        super(Families.movingNonGravityMobFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);

        //position component of player
        PositionComponent ppc = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));

        //accelerate on the x coordinate towards player
        if (pc.position.x < ppc.position.x)
            mc.velocity.x = Math.min(mc.velocityRange.x, mc.velocity.x + mc.acceleration.x);
        else if (pc.position.x > ppc.position.x)
            mc.velocity.x = Math.max(-mc.velocityRange.x, mc.velocity.x - mc.acceleration.x);
        else if (mc.velocity.x != 0) //if entity has same x coordinate as player decrease (absolute value of) velocity.x
            mc.velocity.x -= mc.velocity.x / Math.abs(mc.velocity.x) * mc.acceleration.x;

        //accelerate on the y coordinate towards player
        if (pc.position.y < ppc.position.y)
            mc.velocity.y = Math.min(mc.velocityRange.y, mc.velocity.y + mc.acceleration.y);
        else if (pc.position.y > ppc.position.y)
            mc.velocity.y = Math.max(-mc.velocityRange.y, mc.velocity.y - mc.acceleration.y);
        else if (mc.velocity.y != 0) //if entity has same y coordinate as player decrease (absolute value of) velocity.y
            mc.velocity.y -= mc.velocity.y / Math.abs(mc.velocity.y) * mc.acceleration.y;

        //set position of lbb corner of entity after movement
        pc.position.addi(mc.velocity.scale(deltaTime));
    }
}
