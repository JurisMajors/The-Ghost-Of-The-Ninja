package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.input.KeyBoard;
import group4.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * THis class is the player movement system implemented in such a way
 * that it can be extended to other families, in case you want to apply same movement rules/physics
 * to those entities. (by utilizing the Template Method Pattern)
 * See {@link GhostMovementSystem} as an example for such an extension
 */
public class PlayerMovementSystem extends IteratingSystem {

    public PlayerMovementSystem() {
        super(Families.playerFamily);
    }

    PlayerMovementSystem(Family f) {
        super(f);
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

    /**
     * Adjusts velocity according to the input rules
     * @param e entity to move
     * @param deltaTime frame speed
     */
    private void move(Entity e, float deltaTime) {
        MovementComponent mc = Mappers.movementMapper.get(e);
        Object ref = getMovementRef(e);
        // set velocity in the direction that keyboard asks for
        if (shouldRight(ref)) {
            moveRight(mc);
        } else if (shouldLeft(ref)) {
            moveLeft(mc);
        } else {
            // stay still if no keys are pressed
            mc.velocity.x = 0;
        }
        // jump if space is pressed and if canJump is satisfied
        if (shouldJump(ref) && canJump(mc.velocity)) {
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

    /**
     * Input condition for determining whether to move left
     * @param ref optionally reference object to use for determining this condition
     * @return whether to move entity to the left
     */
    protected boolean shouldLeft (Object ref) {
        return KeyBoard.isKeyDown(GLFW_KEY_A);
    }

    /**
     * Input condition for determining whether to move right.
     * @param ref optionally reference object to use for determining this condition
     * @return whether to move entity to the right
     */
    protected boolean shouldRight (Object ref) {
        return KeyBoard.isKeyDown(GLFW_KEY_D);
    }

    /**
     * Input condition for determining whether to jump.
     * @param ref optionally reference object to use for determining this condition
     * @return whether to move entity to the right
     */
    protected boolean shouldJump (Object ref) {
        return KeyBoard.isKeyDown(GLFW_KEY_SPACE);
    }

    /**
     * Provides a reference object, if one needed for determining
     * the moving conditions of the entity.
     * @param e the entity currently processed
     * @return reference object used to determine how to move
     */
    protected Object getMovementRef(Entity e) {
        return null;
    }

}


