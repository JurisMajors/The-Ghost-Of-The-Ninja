package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

/**
 * stupid name is stupid, should be last
 */
public class LastSystem extends IteratingSystem {

    public LastSystem(int priority) {
        super(Families.gamestateFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // clear all collisions
        if (Mappers.collisionMapper.get(entity) != null) {
            Mappers.collisionMapper.get(entity).collisions.clear();
        }
    }

}
