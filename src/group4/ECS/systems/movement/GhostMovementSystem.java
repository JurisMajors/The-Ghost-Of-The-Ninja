package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import group4.AI.GhostMove;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.etc.Families;

public class GhostMovementSystem extends PlayerMovementSystem {

    public GhostMovementSystem(int priority) {
        super(Families.ghostFamily, priority) ;
    }

    @Override
    protected boolean shouldLeft(Object ref) {
        return GhostMove.LEFT.equals((Integer) ref);
    }

    @Override
    protected boolean shouldRight(Object ref) {
        return GhostMove.RIGHT.equals((Integer) ref);
    }

    @Override
    protected Object getMovementRef(Entity e) {
        GhostComponent ghostComp = e.getComponent(GhostComponent.class);
        Integer move = ghostComp.brain.think();
        ghostComp.moveFreq[move]++;
        return move;
    }

    @Override
    protected boolean shouldJump(Object ref) {
        return GhostMove.JUMP.equals((Integer) ref);
    }

    @Override
    protected boolean shouldSprint() {
        return true;
    }

    @Override
    protected boolean shouldSpawnGhost(Object ref) { return false; }
}
