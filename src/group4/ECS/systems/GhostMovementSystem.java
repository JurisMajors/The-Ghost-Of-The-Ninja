package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import group4.AI.GhostMove;
import group4.ECS.components.GhostComponent;
import group4.ECS.etc.Families;

public class GhostMovementSystem extends PlayerMovementSystem {

    public GhostMovementSystem() {
        super(Families.ghostFamily) ;
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
        return ghostComp.brain.think();
    }

    @Override
    protected boolean shouldJump(Object ref) {
        return GhostMove.JUMP.equals((Integer) ref);
    }

    @Override
    protected boolean shouldSprint() {
        return true;
    }
}
