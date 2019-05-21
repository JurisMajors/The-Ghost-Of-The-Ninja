package group4.ECS.systems.death;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.TheEngine;

public abstract class AbstractDyingSystem extends IteratingSystem {

    AbstractDyingSystem(Family f) {
        super(f);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (shouldDie(entity, deltaTime)) {
            boolean shouldRemove = die(entity, deltaTime);
            if (shouldRemove) {
                TheEngine.getInstance().removeEntity(entity);
            }
        }
    }

    /**
     * Determine whether the entity should die.
     * @param entity the entity to determine its fate
     * @param deltaTime delta time of the game
     * @return whether to kill the entity
     */
    protected abstract boolean shouldDie(Entity entity, float deltaTime);

    /**
     * Death callback for the entity.
     * @param entity  the entity to kill
     * @param deltaTime delta time of the game
     * @return whether to remove the entity from the engine
     */
    protected abstract boolean die(Entity entity, float deltaTime);

}
