package group4.ECS.components.physics;

import com.badlogic.ashley.core.Component;
import group4.ECS.systems.collision.CollisionData;
import group4.ECS.systems.collision.CollisionHandlers.AbstractCollisionHandler;

import java.util.HashSet;
import java.util.Set;

public class CollisionComponent implements Component {

    /**
     * This list holds all entities that are colliding with this entity. When a collision happens the other
     * entity is added to this list. When the collision with that entity is process, that entity is removed
     * from this list again.
     */
    public Set<CollisionData> collisions;

    public AbstractCollisionHandler handler;

    /**
     * CollisionSystem only looks at entities that have this Component
     */
    public CollisionComponent(AbstractCollisionHandler handler) {
        collisions = new HashSet<>();
        this.handler = handler;
    }

}
