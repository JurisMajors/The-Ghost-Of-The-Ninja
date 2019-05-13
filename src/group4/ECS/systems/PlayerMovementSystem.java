package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.input.KeyBoard;
import group4.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerMovementSystem extends IteratingSystem {

    public PlayerMovementSystem() {
        super(Families.playerFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent gc = Mappers.gravityMapper.get(entity);
        // process movement events
        move(entity, deltaTime);
        // apply gravity
        doGravity(mc, gc);
        // move in the specified direction
        pc.position.addi(mc.velocity.scale(deltaTime));

    }

    protected void move(Entity e, float deltaTime) {
        MovementComponent mc = Mappers.movementMapper.get(e);
        // set velocity in the direction that keyboard asks for
        if (KeyBoard.isKeyDown(GLFW_KEY_D)) {
            moveRight(mc);
        } else if (KeyBoard.isKeyDown(GLFW_KEY_A)) {
            moveLeft(mc);
        } else {
            // stay still if no keys are pressed
            mc.velocity.x = 0;
        }
        // jump if space is pressed and if canJump is satisfied
        if (KeyBoard.isKeyDown(GLFW_KEY_SPACE) && canJump(mc.velocity)) {
            jump(mc);
        }
    }

    private void moveRight(MovementComponent mc) {
        mc.velocity.x = mc.velocityRange.x;
    }

    private void moveLeft(MovementComponent mc) {
        mc.velocity.x = -1 * mc.velocityRange.x;
    }

    private void jump(MovementComponent mc) {
        mc.velocity.y = mc.velocityRange.y;
    }

    private boolean canJump(Vector3f velocity) {
        // velocity has to be close to zero (avoid double jumping)
        return velocity.y <= 1e-3 && velocity.y >= -1e-3;
    }

    private void doGravity(MovementComponent mc, GravityComponent gc) {
        mc.velocity.y -= gc.gravity.y;
    }

}


