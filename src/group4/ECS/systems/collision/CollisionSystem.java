package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import group4.ECS.components.CollisionComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

/**
 * This applies collision to entities that can move and have a bounding box
 */
public class CollisionSystem extends IteratingSystem {

    public CollisionSystem() {
        // only process collisions for moving entities that are collidable
        super(Families.collidableMovingFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // don't process entities without a collision component
        if (Mappers.collisionMapper.get(entity) == null) {
            return;
        }

        PositionComponent pc = Mappers.positionMapper.get(entity);

        // detect and store all collisions
        detectCollisions(entity);
    }

    /**
     * Finds all collidable entities that collide with entity e. Stores them in the collision component and
     * lets the next collision system handle them.
     * @param e entity
     */
    private void detectCollisions(Entity e) {
        // get all collidable entities
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.collidableFamily);
        CollisionComponent cc = Mappers.collisionMapper.get(e);

        for (Entity other : entities) {
            if (e.equals(other)) continue;

            // get the intersection between this (moving collidable entity) and other (collidable entity)
            Rectangle intersection = UncollidingSystem.getIntersectingRectangle(e, other);

            // if there is no intersection, do nothing
            if (intersection == null) {
                return;
            }

            CollisionComponent occ = Mappers.collisionMapper.get(other);
            // add the collision to both entities
            cc.collisions.add(other);
            occ.collisions.add(e);
        }

    }

}
