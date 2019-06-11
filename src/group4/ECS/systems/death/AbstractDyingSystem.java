package group4.ECS.systems.death;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.TheEngine;
import group4.game.Main;

public abstract class AbstractDyingSystem extends IteratingSystem {

    AbstractDyingSystem(Family f, int priority) {
        super(f, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (shouldDie(entity, deltaTime)) {
            boolean shouldRemove = die(entity, deltaTime);
            if (!Main.AI) {
                sound();
            }
            if (shouldRemove) {
                TheEngine.getInstance().removeEntity(entity);
                // TODO: remove from module entity list too
                // requires global module..
            }
        }
    }

    /**
     * Determine whether the entity should die.
     *
     * @param entity    the entity to determine its fate
     * @param deltaTime delta time of the game
     * @return whether to kill the entity
     */
    protected abstract boolean shouldDie(Entity entity, float deltaTime);

    /**
     * Death callback for the entity.
     *
     * @param entity    the entity to kill
     * @param deltaTime delta time of the game
     * @return whether to remove the entity from the engine
     */
    protected abstract boolean die(Entity entity, float deltaTime);

    /**
     * Play sound after death
     */
    protected void sound() {
        // no sound by default
    }

}
