package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.PositionComponent;
import group4.ECS.entities.Ghost;
import com.badlogic.ashley.core.Entity;
import group4.AI.GhostMove;
import group4.ECS.components.GhostComponent;
import group4.ECS.etc.Families;

public class GhostDyingSystem extends PlayerDyingSystem {

    public GhostDyingSystem (boolean reset) {
        super(Families.ghostFamily, reset);
    }

    public GhostDyingSystem () {
        super(Families.ghostFamily, false);
    }

    @Override
    protected boolean shouldDie(Entity entity, float deltaTime) {
        int[] moveFreq = entity.getComponent(GhostComponent.class).moveFreq;
        boolean tooDumb = false;
        int argMax = 0;
        int zerosCount = 0;
        for (int i = 0; i < moveFreq.length ; i++) {
            if (moveFreq[i] > moveFreq[argMax]) {
                argMax = i;
            }
            if (moveFreq[i] == 0) {
                zerosCount ++;
            }
        }
        tooDumb = (moveFreq[argMax] > 50 && zerosCount == 2);
        return super.shouldDie(entity, deltaTime) || tooDumb;
    }

    @Override
    protected boolean die(Entity entity, float deltaTime) {
        Ghost g = (Ghost) entity;
        g.getComponent(PositionComponent.class).position = g.level.getCurrentModule().getPlayerInitialPosition();
        return false;
    }
}
