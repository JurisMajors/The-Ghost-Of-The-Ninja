package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.IntersectionPair;
import group4.maths.Ray;
import group4.maths.Vector3f;
import group4.utils.DebugUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class MobMovementSystem extends IteratingSystem {

    public MobMovementSystem(Family family) {
        super(family);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        GravityComponent gc = Mappers.gravityMapper.get(entity);

        PositionComponent playerPos;
        // send rays and check if the mob can see the player
        if (canSeePlayer(360, 36, pc, dc, mc)) {
            // get the player position
            playerPos = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        } else {
            playerPos = new PositionComponent(pc.position);
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

    protected boolean canSeePlayer(float angleRange, int nrRays, PositionComponent pc, DimensionComponent dc, MovementComponent mc) {
        List<Class<? extends Component>> seeThrough = new ArrayList<>();
        seeThrough.add(MobComponent.class);

        // look in the direction the mob is moving
        Vector3f dir = new Vector3f(mc.velocity);
        // center of the mob
        Vector3f center = pc.position.add(dc.dimension.scale(0.5f));

        // no velocity means hes looking to the top
        if (Math.abs(dir.length()) < 1e04) {
            dir = new Vector3f(0, 1, 0);
        }

        float deltaTheta = angleRange / (float) nrRays;
        // start at the bottom of the range
        dir = dir.rotateXY(-1f * (angleRange / 2.0f));

        for (int i = 0; i < nrRays; i++) {
            Ray ray = new Ray(center, dir, seeThrough, 2f);
            IntersectionPair ip = ray.cast(TheEngine.getInstance().getEntitiesFor(Families.allCollidableFamily));

            // if this ray reaches the player
            if (ip.entity instanceof Player && !(ip.entity instanceof Ghost)) {
                return true;
            }

            // debug drawing the vision
            if (ip.point != null) {
                DebugUtils.drawLine(center, ip.point);
            }

            // rotate the next ray
            dir = dir.rotateXY(deltaTheta);
        }

        return false;
    }
}
