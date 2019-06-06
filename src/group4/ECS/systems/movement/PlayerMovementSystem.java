package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.etc.EntityState;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.input.KeyBoard;
import group4.input.MouseMovement;
import group4.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * THis class is the player movement system implemented in such a way
 * that it can be extended to other families, in case you want to apply same movement rules/physics
 * to those entities. (by utilizing the Template Method Pattern)
 * See {@link GhostMovementSystem} as an example for such an extension
 */
public class PlayerMovementSystem extends IteratingSystem {
    private boolean wasSpaceDown = false;
    private boolean jumpInProgress = false;

    public PlayerMovementSystem(int priority) {
        super(Families.playerFamily, priority);
    }

    public PlayerMovementSystem(Family f, int priority) {
        super(f, priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent gc = Mappers.gravityMapper.get(entity);
        // process movement events
        move(entity, mc, pc, deltaTime);
        // apply gravity
        doGravity(mc, gc);
        // move in the specified direction
        pc.position.addi(mc.velocity);
    }

    /**
     * Adjusts velocity according to the input rules
     *
     * @param e         entity to move
     * @param deltaTime frame speed
     */
    protected void move(Entity e, MovementComponent mc, PositionComponent pc, float deltaTime) {
        // Get and "unpack" some much used objects
        Object ref = getMovementRef(e);
        HierarchicalPlayer player = (HierarchicalPlayer) e;
        EntityState playerState = player.getState();

        // set velocity in the direction that keyboard asks for
        if (shouldRight(ref)) {
            moveRight(mc, pc);
        } else if (shouldLeft(ref)) {
            moveLeft(mc, pc);
        } else {
            // stay still if no keys are pressed
            mc.velocity.x = 0;
        }

        // If we are NOT in the jump or landing sequence, AND we ARE on a platform, then we are idle/walking/running.
        boolean wandering = playerState != EntityState.PLAYER_POSTFALL && playerState != EntityState.PLAYER_PREJUMP && pc.onPlatform;
        if (wandering) {
            if (Math.abs(mc.velocity.x) > 1e-3) {
                if (shouldSprint()) {
                    player.setState(EntityState.PLAYER_RUNNING);
                } else {
                    player.setState(EntityState.PLAYER_WALKING);
                }
            } else {
                player.setState(EntityState.PLAYER_IDLE);
            }
        }

        // jump if space is pressed and if canJump is satisfied
        if (shouldJump(ref) && canJump(mc.velocity) && pc.onPlatform) {
            player.setState(EntityState.PLAYER_PREJUMP);
        }

        // Catch if we have transitioned to the actual jump phase of our jump
        if (!jumpInProgress && playerState == EntityState.PLAYER_JUMPING) {
            jump(mc);
            jumpInProgress = true;
        }

        // If we're tracking the jump is in progress, but it obviously isn't, toggle it off.
        if (jumpInProgress && playerState != EntityState.PLAYER_JUMPING) {
            jumpInProgress = false;
        }

        // Check if player is falling to animate a fall
        if (mc.velocity.y < -1e-3) {
            player.setState(EntityState.PLAYER_FALLING);
        }

        // Check if player has landed on a platform for animation
        if (((Player) e).getState() == EntityState.PLAYER_FALLING && pc.onPlatform) {
            player.setState(EntityState.PLAYER_POSTFALL);
        }

        if (shouldSpawnGhost(ref) && !((Player) e).spawnedGhost) {
            ((Player) e).spawnedGhost = true;
            ((Player) e).level.getCurrentModule().addGhost((Player) e);
        }

        // Finally limit the velocity vector
        mc.velocity.capValuesi(mc.velocityRange);
    }

    private void moveRight(MovementComponent mc, PositionComponent pc) {
        moveDirection(1, mc, pc);
    }

    private void moveLeft(MovementComponent mc, PositionComponent pc) {
        moveDirection(-1, mc, pc);
    }

    /**
     * Moves along the x axis in the specified direction
     */
    private void moveDirection(int moveDir, MovementComponent mc, PositionComponent pc) {
        // set orientation of player in accordance to mouse position
        if (pc.position.x <= MouseMovement.mouseX) {
            mc.orientation = MovementComponent.RIGHT;
        } else {
            mc.orientation = MovementComponent.LEFT;
        }

        if (shouldSprint() && canSprint(mc.velocity)) {
            mc.velocity.x = moveDir * getSprintingVel(mc);
            mc.velocity.x += moveDir * mc.acceleration.x;
        } else {
            // de-accelerate if necessary
            mc.velocity.x = moveDir * getWalkingVel(mc);
        }
    }

    private float getSprintingVel(MovementComponent mc) {
        // provides with the "starting" or "current" sprinting velocity
        return Math.max(mc.velocityRange.x * Player.walkingRatio, Math.abs(mc.velocity.x));
    }

    private float getWalkingVel(MovementComponent mc) {
        return Math.max(mc.velocityRange.x * Player.walkingRatio, Math.abs(mc.velocity.x) - mc.acceleration.x);
    }

    private void jump(MovementComponent mc) {
        mc.velocity.y = mc.velocityRange.y;
    }

    private boolean canJump(Vector3f velocity) {
        // velocity has to be close to zero (avoid double jumping)
        return velocity.y <= 1e-3 && velocity.y >= -1e-3;
    }

    private boolean canSprint(Vector3f velocity) {
        return velocity.y <= 1e-3 && velocity.y >= -1e-3;
    }


    private void doGravity(MovementComponent mc, GravityComponent gc) {
        mc.velocity.y -= gc.gravity.y;
    }

    /**
     * Whether the entity should accelerate when he moves
     */
    protected boolean shouldSprint() {
        return KeyBoard.isKeyDown(GLFW_KEY_LEFT_SHIFT);
    }

    /**
     * Input condition for determining whether to move left
     *
     * @param ref optionally reference object to use for determining this condition
     * @return whether to move entity to the left
     */
    protected boolean shouldLeft(Object ref) {
        return KeyBoard.isKeyDown(GLFW_KEY_A);
    }

    /**
     * Input condition for determining whether to move right.
     *
     * @param ref optionally reference object to use for determining this condition
     * @return whether to move entity to the right
     */
    protected boolean shouldRight(Object ref) {
        return KeyBoard.isKeyDown(GLFW_KEY_D);
    }

    /**
     * Input condition for determining whether to jump.
     *
     * @param ref optionally reference object to use for determining this condition
     * @return whether to move entity to the right
     */
    protected boolean shouldJump(Object ref) {
        boolean spaceDown = KeyBoard.isKeyDown(GLFW_KEY_SPACE); // Get current space bar status

        if (wasSpaceDown && !spaceDown) { // Space bar up, and was down, so update wasSpaceDown, and return not to jump
            wasSpaceDown = false;
            return false;
        } else if (!wasSpaceDown && spaceDown) { // Space bar was not down, now it's down, so update wasSpaceDown and jump
            wasSpaceDown = true;
            return true;
        }

        return false; // In all other cases, we do not jump or update
    }

    protected boolean shouldSpawnGhost(Object ref) {
        return KeyBoard.isKeyDown(GLFW_KEY_G);
    }

    /**
     * Provides a reference object, if one needed for determining
     * the moving conditions of the entity.
     *
     * @param e the entity currently processed
     * @return reference object used to determine how to move
     */
    protected Object getMovementRef(Entity e) {
        return null;
    }
}


