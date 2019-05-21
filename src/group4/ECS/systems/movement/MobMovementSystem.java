package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.SplinePathComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public abstract class MobMovementSystem extends IteratingSystem {

    public MobMovementSystem(Family family) {
        super(family);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent gc = Mappers.gravityMapper.get(entity);

        PositionComponent playerPos;

        // splines mobs have a different target than normal mobs
        if (isSplineMob(entity)) {
            SplinePathComponent spc = Mappers.splinePathMapper.get(entity);
            // get the direction and position to move towards
            Vector3f targetDirection = spc.targetDirection(pc.position, dc.dimension, mc.velocity);
            Vector3f targetPos = pc.position.add(targetDirection);

            // the target position is currently stored in playerPos, this name should probably change
            playerPos = new PositionComponent(targetPos);


            // TODO: follow the player if the player is in sight
        } else {
            // get the player position
            playerPos = getTargetPositionComponent();
        }

        // process movement events
        move(entity, playerPos, deltaTime);
        // apply gravity
        doGravity(mc, gc);
        // move in the specified direction
        pc.position.addi(mc.velocity.scale(deltaTime));
    }

    protected void move(Entity e, PositionComponent playerPos, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);

        // set velocity.x in the direction towards the player
        if (pc.position.x < playerPos.position.x && canMoveRight(mc.velocity)) {
            moveRight(mc);
        } else if (pc.position.x > playerPos.position.x && canMoveLeft(mc.velocity)) {
            moveLeft(mc);
        } else {
            mc.velocity.x = 0;
        }

        // set velocity.y in the direction towards the player
        if (pc.position.y < playerPos.position.y && canJump(mc.velocity)) {
            jump(mc);
        } else if (pc.position.y > playerPos.position.y && canMoveDown()) {
            moveDown(mc);
        } else if (canJump(mc.velocity) && canMoveDown()) mc.velocity.y = 0;

    }

    /**
     * Returns the position component containing the position that this mob should move towards.
     *
     * @return position component
     */
    protected PositionComponent getTargetPositionComponent() {
        return Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
    }

    protected void moveRight(MovementComponent mc) {
        mc.velocity.x = mc.velocityRange.x;
    }

    protected void moveLeft(MovementComponent mc) {
        mc.velocity.x = -1 * mc.velocityRange.x;
    }

    protected void moveDown(MovementComponent mc) {
        mc.velocity.y = -mc.velocityRange.y;
    }

    protected void jump(MovementComponent mc) {
        mc.velocity.y = mc.velocityRange.y;
    }

    protected boolean canMoveLeft(Vector3f velocity) {
        return true;
    }

    protected boolean canMoveRight(Vector3f velocity) {
        return true;
    }

    protected boolean canMoveDown() {
        return false;
    }

    protected boolean canJump(Vector3f velocity) {
        // velocity has to be close to zero (avoid double jumping)
        return velocity.y <= 1e-3 && velocity.y >= -1e-3;
    }

    protected void doGravity(MovementComponent mc, GravityComponent gc) {
        mc.velocity.y -= gc.gravity.y;
    }

    protected final boolean isSplineMob(Entity e) {
        SplinePathComponent spc = Mappers.splinePathMapper.get(e);
        return spc != null;
    }
}
