package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.components.StatsComponent;
import group4.ECS.entities.Player;
import group4.ECS.etc.Families;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class PlayerDyingSystem extends AbstractDyingSystem {

    public PlayerDyingSystem() {
        super(Families.playerFamily);
    }

    @Override
    protected boolean shouldDie(Entity entity, float deltaTime) {
        // Check if entity is indeed the player
        if (Player.class.isInstance(entity)) {
            // Get the position and dimension components
            PositionComponent pp = entity.getComponent(PositionComponent.class);
            DimensionComponent pd = entity.getComponent(DimensionComponent.class);

            // Compute player center
            Vector3f pc = pp.position.add(pd.dimension.scale(0.5f));

            // Check whether or not the player's center is outside the module grid
            // We keep a 1 grid-unit boundary which the player's center can legally move out of the grid
            if (pc.x < -1 || pc.x > Module.width + 1 || pc.y < -1 || pc.y > Module.height + 1) {
                return true;
            }
        }

        // There is no reason to kill the entity
        return false;
    }

    @Override
    protected boolean die(Entity entity, float deltaTime) {
        // Kill the player, i.e. set its health to 0
        if (Player.class.isInstance(entity)) {
            entity.getComponent(StatsComponent.class).health = 0;
        }

        System.out.println("Doei player");

        // Level should take care of removing a player from the engine if that is necessary
        // Return that player should not be removed
        return false;
    }

}
