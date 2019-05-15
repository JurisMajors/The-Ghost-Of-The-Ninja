package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.CollisionComponent;

/**
 * This is an abstract class for resolving collisions of entities
 * It is advised to declare a Singletone static method, in order to not create many objects (in case you expect
 * many duplicate objects), since a reference is stored within every collision component.
 * E.g. {@link PlayerCollision}, {@link MobCollision}.
 *
 * @param <T> the type of entity to resolve the collision for
 */
public abstract class AbstractCollisionHandler<T extends Entity> {

    /**
     * This function specifies how a player deals with different types of collisions.
     *
     * @param e is the entity subclass which to resolve the collision
     * @param cc the collision component of the entity
     */
    public abstract void collision(T e, CollisionComponent cc);

}
