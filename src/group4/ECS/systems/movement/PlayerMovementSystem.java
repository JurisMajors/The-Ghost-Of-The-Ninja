package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.components.stats.ScoreComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.entities.totems.Totem;
import group4.ECS.etc.*;
import group4.audio.Sound;
import group4.game.Main;
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
        this.move(entity, mc, pc, deltaTime);

        // Finally, update the animation
        this.updateState(entity, mc, pc);

        // apply gravity
        this.doGravity(mc, gc);

        this.ghostSpawning(entity);

        // move in the specified direction
        pc.position.addi(mc.velocity);
    }

    /**
     * This class takes in an Entity and its movement- and positioncomponents. According to the current state of the
     * Entity and the movements/inputs applied in this update cycle, we determine a new EntityState with which to update.
     *
     * @param e Entity, for which we update the EnityState
     */
    protected void updateState(Entity e, MovementComponent mc, PositionComponent pc) {
        // Get and "unpack" some much used objects
        Object ref = getMovementRef(e);
        Player player = (Player) e;
        EntityState playerState = player.getState();
        EntityState nextState = null;

        // If we are NOT in the jump or landing sequence, AND we ARE on a platform, then we are idle/walking/running.
        boolean wandering = playerState != EntityState.PLAYER_POSTFALL &&
                playerState != EntityState.PLAYER_FALLING &&
                playerState != EntityState.PLAYER_PREJUMP &&
                pc.onPlatform && !jumpInProgress;

        // We are either wandering, or not. See the state diagram.
        ((HierarchicalPlayer) player).getTorso().rotation = 0;  // Reset torso rotation angle
        if (wandering) {
            if (Math.abs(mc.velocity.x) > 1e-3) {
                if (shouldSprint() && canSprint(pc)) {
                    nextState = EntityState.PLAYER_RUNNING;
                    ((HierarchicalPlayer) player).getTorso().rotation = 15;  // Running player
                } else {
                    nextState = EntityState.PLAYER_WALKING;
                }
            } else {
                nextState = EntityState.PLAYER_IDLE;
            }

            // jump if space is pressed and if canJump is satisfied
            if (shouldJump(ref) && canJump(pc)) {
                nextState = EntityState.PLAYER_PREJUMP;
            }
        } else {
            // Check if player is falling to animate a fall
            if (mc.velocity.y < -1e-3) {
                nextState = EntityState.PLAYER_FALLING;
            }

            // Check if player has landed on a platform for animation
            if (((Player) e).getState() == EntityState.PLAYER_FALLING && pc.onPlatform) {
                nextState = EntityState.PLAYER_POSTFALL;
            }
        }

        if (nextState != null) {
            player.setState(nextState);
        }
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
        Player player = (Player) e;
        EntityState playerState = player.getState();


        // if entity is not being knocked back
        if (!Mappers.healthMapper.get(e).state.contains(EntityConst.EntityState.KNOCKED)) {
            if (shouldRight(ref)) {
                moveRight(mc, pc);
            } else if (shouldLeft(ref)) {
                moveLeft(mc, pc);
            } else {
                // stay still if no keys are pressed
                mc.velocity.x = 0;
            }

            if (!jumpInProgress && playerState == EntityState.PLAYER_JUMPING) {
                // Catch if we have transitioned to the actual jump phase of our jump
                jump(mc);
                jumpInProgress = true;
            }

            // If we're tracking the jump is in progress, but it obviously isn't, toggle it off.
            if (jumpInProgress && playerState != EntityState.PLAYER_JUMPING) {
                jumpInProgress = false;
            }
        }

        // Finally limit the velocity vector
        mc.velocity.capValuesi(mc.velocityRange);
    }

    private void ghostSpawning(Entity e) {
        Player player = (Player) e;
        int score = player.getComponent(ScoreComponent.class).getScore();
        // if the player previous ghost is not alive and player is on a start totem
        if (!player.spawnedGhost && player.totemStatus != null) {
            boolean spawned = false; // whether player decided to spawn a ghost
            Ghost newGhost = null; // the spawned ghost
            Vector3f mask = new Vector3f(); // the global mask to apply for the world

            // process keypresses/conditions for spawning
            if (challangeGhost()) {
                spawned = true;
                newGhost = player.totemStatus.getChallangeGhost(player); // get a challanger ghost from the totem
                player.startTotemID = player.totemStatus.getID();
                mask.x = 0.2f;
            } else if (helpGhost(score)) {
                spawned = true;
                player.getComponent(ScoreComponent.class).subScore(Totem.HELPCOST);
                newGhost = player.totemStatus.getHelpGhost(player);
                mask.z = 0.2f;
            } else if (carryGhost(score)) {
                spawned = true;
                player.getComponent(ScoreComponent.class).subScore(Totem.CARRYCOST);
                newGhost = player.totemStatus.getCarryGhost(player);
                mask.z = 0.5f;
            }
            if (spawned) {
                player.spawnedGhost = true;
                // set a background color while in the 'ghost' world
                GraphicsComponent.setGlobalColorMask(mask);
                // add it to engine an module
                player.level.getCurrentModule().addEntity(newGhost);
                TheEngine.getInstance().addEntity(newGhost);
            }
        }
    }

    protected boolean helpGhost(int score) {
        return KeyBoard.isKeyDown(GLFW_KEY_G) && score >= Totem.HELPCOST;
    }

    protected boolean challangeGhost() {
        return KeyBoard.isKeyDown(GLFW_KEY_C);
    }

    protected boolean carryGhost(int score) {
        return KeyBoard.isKeyDown(GLFW_KEY_H) && score >= Totem.CARRYCOST;
    }

    private void moveRight(MovementComponent mc, PositionComponent pc) {
        moveDirection(MovementComponent.RIGHT, mc, pc);
        mc.setOrientation(MovementComponent.RIGHT);
    }

    private void moveLeft(MovementComponent mc, PositionComponent pc) {
        moveDirection(MovementComponent.LEFT, mc, pc);
        mc.setOrientation(MovementComponent.LEFT);
    }

    /**
     * Moves along the x axis in the specified direction
     */
    private void moveDirection(int moveDir, MovementComponent mc, PositionComponent pc) {
        if (!Main.AI && !Sound.isPlaying(Sound.STEP) && canJump(pc)) {
            Sound.playRandom(Sound.STEP);
        }

        if (shouldSprint() && canSprint(pc)) {
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
        // TODO: Play jump sound
        mc.velocity.y = mc.velocityRange.y;
    }

    private boolean canJump(PositionComponent pc) {
        return pc.onPlatform;
    }

    private boolean canSprint(PositionComponent pc) {
        return pc.onPlatform;
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


