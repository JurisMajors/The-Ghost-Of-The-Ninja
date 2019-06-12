package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import group4.AI.GhostMove;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.maths.Vector3f;

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
    protected boolean helpGhost(int score) { return false; }
    @Override
    protected boolean challangeGhost() { return false; }
    @Override
    protected boolean carryGhost(int score) { return false; }

    @Override
    protected void move(Entity e, MovementComponent mc, PositionComponent pc, float deltaTime) {
        boolean shouldMove = true; // whether the ghost should move this turn
        if (((Ghost) e).helping && pc.onPlatform) { // if ghost is helper and is on a platform
            // check if he is on screen, if so, then do nothing, otherwise stop him
            // get camera info
            Entity mainCamera = TheEngine.getInstance().getEntitiesFor(Families.cameraFamily).get(0);
            Vector3f mainCameraPosition = mainCamera.getComponent(PositionComponent.class).position;

            // Check whether the ghost is off screen
            if (!(pc.position.x <= mainCameraPosition.x + Main.SCREEN_WIDTH / 2
                    && pc.position.x >= mainCameraPosition.x - Main.SCREEN_WIDTH / 2
                    && pc.position.y <= mainCameraPosition.y + Main.SCREEN_HEIGHT / 2
                    && pc.position.y >= mainCameraPosition.y - Main.SCREEN_HEIGHT / 2)) {
                shouldMove = false;
            }
        }
        if (shouldMove) super.move(e, mc, pc, deltaTime);
        if (((Ghost) e).carrying) { // this ghost is a carrier ghost, then move the player with the ghost
            ((Ghost) e).master.getComponent(PositionComponent.class).position = new Vector3f(pc.position);
        }
    }
}
