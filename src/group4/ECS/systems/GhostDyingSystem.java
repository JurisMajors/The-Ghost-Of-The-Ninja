package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.PositionComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.etc.Families;

public class GhostDyingSystem extends PlayerDyingSystem {

    public GhostDyingSystem (boolean reset) {
        super(Families.ghostFamily, reset);
    }

    public GhostDyingSystem () {
        super(Families.ghostFamily, false);
    }


    @Override
    protected boolean die(Entity entity, float deltaTime) {
        Ghost g = (Ghost) entity;
        g.getComponent(PositionComponent.class).position = g.level.getCurrentModule().getPlayerInitialPosition();
        return false;
    }
}
