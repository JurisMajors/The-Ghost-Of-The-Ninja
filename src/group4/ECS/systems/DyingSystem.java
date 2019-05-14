package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.TheEngine;

/**
 * Abstract class for processing death of an entity.
 */
public abstract class DyingSystem extends IteratingSystem {

    DyingSystem(Family f) {
        super(f);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (shouldDie(entity, deltaTime)) {
            die(entity, deltaTime);
            TheEngine.getInstance().removeEntity(entity);
        }
    }

    /**
     * Condition of death
     * @param e the entity whose fate is determined
     * @param deltaTime frame speed
     * @return whether entity should be removed from the engine.
     */
    protected abstract boolean shouldDie (Entity e, float deltaTime);

    /**
     * Callback for death of the entity
     * Note, that this method does not need to remove from the engine,
     * it is done within processEntity, after this method.
     * @param e entity to kill
     * @param deltaTime frame speed
     */
    protected abstract void die (Entity e, float deltaTime);

}
