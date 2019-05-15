package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.Player;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionHandlers.PlayerCollision;

public class CollisionEventSystem extends IteratingSystem {

    public CollisionEventSystem() {
        super(Families.collidableMovingFamily);
    }

    /**
     * Here we decide which event handler should handle the collision events for this entity.
     *
     * @param entity    current entity
     * @param deltaTime
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent cc = Mappers.collisionMapper.get(entity);
        // no collisions to process
        if (cc.collisions.isEmpty()) {
            return;
        }

        cc.handler.collision(entity, cc);
    }
}
