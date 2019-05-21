package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class BulletMovementSystem extends IteratingSystem {

    public BulletMovementSystem() {
        super(Families.bulletFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        //bullets have constant velocity
        pc.position.addi(mc.velocity.scale(deltaTime));
    }
}
