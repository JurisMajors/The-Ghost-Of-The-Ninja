package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class UncollidingSystem extends IteratingSystem {

    public UncollidingSystem(int priority) {
        super(Families.allCollidableFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // clear all collisions
        Mappers.collisionMapper.get(entity).collisions.clear();
    }

}
