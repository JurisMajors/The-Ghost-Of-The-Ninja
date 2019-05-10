package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class PlayerMovementSystem extends IteratingSystem {

    public PlayerMovementSystem() {
        super(Families.movingPlayerFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = Mappers.positionMapper.get(entity);
        MovementComponent movementComponent = Mappers.movementMapper.get(entity);

        movementComponent.velocity.addi(movementComponent.acceleration.scale(deltaTime));
        positionComponent.position.addi(movementComponent.velocity.scale(deltaTime));
    }
}
