package group4.ECS.systems.death;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.etc.Families;
import group4.game.Main;

public class GhostDyingSystem extends PlayerDyingSystem {

    public GhostDyingSystem (boolean reset, int priority) {
        super(Families.ghostFamily, reset, priority);
    }

    public GhostDyingSystem (int priority) {
        super(Families.ghostFamily, false, priority);
    }

    @Override
    protected boolean shouldDie(Entity entity, float deltaTime) {
        int[] moveFreq = entity.getComponent(GhostComponent.class).moveFreq;
        int argMax = 0;
        int zerosCount = 0;
        // find freq of best move and how many moves are zero
        for (int i = 0; i < moveFreq.length ; i++) {
            if (moveFreq[i] > moveFreq[argMax]) {
                argMax = i;
            }
            if (moveFreq[i] == 0) {
                zerosCount ++;
            }
        }
        // if best move has more than 50 calls, but there are still
        // two moves with zero, this individual is dumb :)
        boolean tooDumb = (moveFreq[argMax] > 80 && zerosCount >= 2);
        return super.shouldDie(entity, deltaTime) || (Main.AI && tooDumb);
    }

    @Override
    protected boolean die(Entity entity, float deltaTime) {
        Ghost g = (Ghost) entity;
        if (!Main.AI) {
            g.getComponent(HealthComponent.class).health = 0;
            g.master.spawnedGhost = false; // can spawn again
            g.master.challanging = false;
            return true;
        } else {
            g.getComponent(HealthComponent.class).health = 0;
        }
        return false;
    }
}
