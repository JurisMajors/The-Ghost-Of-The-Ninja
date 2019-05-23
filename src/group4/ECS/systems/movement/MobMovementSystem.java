package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class MobMovementSystem extends IteratingSystem {

    public MobMovementSystem() {
        super(Families.mobFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MobComponent mobComponent = Mappers.mobMapper.get(entity);

        mobComponent.handler.handleMovement(entity, deltaTime);
    }
}
