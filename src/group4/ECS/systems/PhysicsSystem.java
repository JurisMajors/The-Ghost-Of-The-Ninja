package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.GravityComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class PhysicsSystem extends IteratingSystem {

    public PhysicsSystem() {
        super(Families.physicsFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent movementComponent = Mappers.movementMapper.get(entity);
        GravityComponent gravityComponent = Mappers.physicsMapper.get(entity);

        movementComponent.velocity.addi(gravityComponent.gravity.scale(deltaTime));
    }
}
