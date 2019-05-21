package group4.ECS.systems.death;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.Families;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class PlayerDyingSystem extends AbstractDyingSystem {

    private boolean autoReset;

    /**
     * Initialize the PlayerDyingSystem
     * @param reset Indicate whether the system should automatically reset the module once the player dies
     */
    public PlayerDyingSystem(boolean reset) {
        this(Families.playerFamily, reset);
    }
    PlayerDyingSystem(Family f, boolean reset) {
        super(f);
        this.autoReset = reset;
    }

    @Override
    protected boolean shouldDie(Entity entity, float deltaTime) {
        // Get the position and dimension components
        PositionComponent pp = entity.getComponent(PositionComponent.class);
        DimensionComponent pd = entity.getComponent(DimensionComponent.class);

        // Compute player center
        Vector3f pc = pp.position.add(pd.dimension.scale(0.5f));
        Module curModule = entity instanceof Player ? ((Player)entity).level.getCurrentModule()
                : ((Mob) entity).level.getCurrentModule();
        // Check whether or not the player's center is outside the module grid
        // We keep a 1 grid-unit boundary which the player's center can legally move out of the grid
        if (pc.x < -1 || pc.x > curModule.getWidth() + 1 ||
                pc.y < -1 || pc.y > curModule.getHeight() + 1) {
            return true;
        }

        // If the players health is 0, kill it as well
        if (entity.getComponent(HealthComponent.class).health <= 0) {
            return true;
        }

        // There is no reason to kill the player
        return false;
    }

    @Override
    protected boolean die(Entity entity, float deltaTime) {
        entity.getComponent(HealthComponent.class).health = 0;

        // If auto reset is enabled, reset the module to its original state
        // and reposition the player, while giving it new health
        if (this.autoReset) {
            ((Player) entity).level.getCurrentModule().reset();
            ((Player) entity).getComponent(PositionComponent.class).position =
                    ((Player) entity).level.getCurrentModule().getPlayerInitialPosition();
            ((Player) entity).getComponent(HealthComponent.class).health = 100;
        }


        // Level should take care of removing a player from the engine if that is necessary
        // Return that player should not be removed
        return false;
    }

}