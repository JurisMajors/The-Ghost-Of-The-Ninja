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
        PositionComponent positionComponent = Mappers.positionMapper.get(entity);
        MovementComponent movementComponent = Mappers.movementMapper.get(entity);
        PositionComponent playerPositionComponent = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));

        if (positionComponent.position.x < playerPositionComponent.position.x)
            movementComponent.velocity.x = Math.min(movementComponent.velocityRange.x, movementComponent.velocity.x + movementComponent.acceleration.x);
        if (positionComponent.position.x > playerPositionComponent.position.x)
            movementComponent.velocity.x = Math.max(-movementComponent.velocityRange.x, movementComponent.velocity.x - movementComponent.acceleration.x);
        if (positionComponent.position.y < playerPositionComponent.position.y)
            movementComponent.velocity.y = Math.min(movementComponent.velocityRange.y, movementComponent.velocity.y + movementComponent.acceleration.y);
        if (positionComponent.position.y > playerPositionComponent.position.y)
            movementComponent.velocity.y = Math.max(-movementComponent.velocityRange.y, movementComponent.velocity.y - movementComponent.acceleration.y);

        positionComponent.position.addi(movementComponent.velocity.scale(deltaTime));
    }
}
