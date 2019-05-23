package group4.ECS.systems.movement.MovementHandlers;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import group4.ECS.components.SplinePathComponent;
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


public abstract class AbstractMovementHandler<T extends Entity> {

    public void handleMovement(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent gc = Mappers.gravityMapper.get(entity);

        // the position that this entity wants to move towards
        Vector3f targetPosition;
        // send rays and check if the mob can see the player
        if (canSeePlayer(360, 36, pc, dc, mc)) {
            // set target to the player position
            targetPosition = getPlayerPosition();

            // make sure that were not following a spline anymore
            if (isSplineMob(entity)) {
                SplinePathComponent spc = Mappers.splinePathMapper.get(entity);
                spc.leaveSpline();
            }
        } else if (isSplineMob(entity)) {
            // splines mobs have a different target than normal mobs
            SplinePathComponent spc = Mappers.splinePathMapper.get(entity);

            if (spc.isOnSpline()) {
                // do movement using the spline path system
                spc.updateU(deltaTime);
                pc.position = spc.getPoint();
                targetPosition = pc.position;
            } else {
                // move towards the starting point of the spline
                targetPosition = spc.points[0];

                // check if we arrived at the spline
                if (closeToStart(pc, spc)) {
                    spc.startSpline();
                }
            }
        } else {
            // don't move
            targetPosition = pc.position;
        }

        // process movement events
        move(entity, targetPosition, deltaTime);
        // apply gravity
        doGravity(mc, gc);

        pc.position.addi(mc.velocity.scale(deltaTime));
    }

    protected void move(Entity e, Vector3f targetPosition, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);

        // set velocity.x in the direction towards the player
        if (Math.abs(pc.position.x - targetPosition.x) <= Math.abs(mc.velocity.x)) {
            // if close enough, don't overshoot
            mc.velocity.x = targetPosition.x - pc.position.x;
        } else if (pc.position.x < targetPosition.x && canMoveRight(mc.velocity)) {
            moveRight(mc);
        } else if (pc.position.x > targetPosition.x && canMoveLeft(mc.velocity)) {
            moveLeft(mc);
        } else {
            mc.velocity.x = 0;
        }

        // set velocity.y in the direction towards the player
        if (Math.abs(pc.position.y - targetPosition.y) < Math.abs(mc.velocity.y)) {
            // if close enough, don't overshoot
            mc.velocity.y = targetPosition.y - pc.position.y;
        } else if (pc.position.y < targetPosition.y && canJump(mc.velocity)) {
            jump(mc);
        } else if (pc.position.y > targetPosition.y && canMoveDown()) {
            moveDown(mc);
        } else if (canJump(mc.velocity) && canMoveDown()) {
            mc.velocity.y = 0;
        }

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

    private Vector3f getPlayerPosition() {
        return Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0)).position;
    }

    private boolean canSeePlayer(float angleRange, int nrRays, PositionComponent pc, DimensionComponent dc, MovementComponent mc) {
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

            DebugUtils.drawLine(center, ip.point);

            // rotate the next ray
            dir = dir.rotateXY(deltaTheta);
        }

        return false;
    }

    protected final boolean isSplineMob(Entity e) {
        SplinePathComponent spc = Mappers.splinePathMapper.get(e);
        return spc != null;
    }


    /// BELOW ARE FUNCTIONS THAT DEAL WITH GUIDING A MOB ALONG A SPLINE

    private boolean closeToStart(PositionComponent pc, SplinePathComponent spc) {
        float dist = pc.position.sub(spc.points[0]).length();
        return dist < 0.1;
    }


    /**
     * Gets a copy of the closest spline point to the center of the given entity.
     * The center is the position + 0.5 * dimension
     *
     * @param points    spline approximation points
     * @param position  the position of the entity
     * @param dimension the dimension of the entity
     * @return
     */
    private Vector3f getClosestSplinePoint(Vector3f[] points, Vector3f position, Vector3f dimension) {
        return points[getClosestSplinePointIndex(points, position, dimension)];
    }

    /**
     * Gets the index of the closest spline point to the center of the given entity.
     * The center is the position + 0.5 * dimension
     *
     * @param position  the position of the entity
     * @param dimension the dimension of the entity
     * @return
     */
    private int getClosestSplinePointIndex(Vector3f[] points, Vector3f position, Vector3f dimension) {
//        Vector3f center = position.add(dimension.scale(0.5f));
        Vector3f center = position;

        float minDistance = Float.MAX_VALUE;
        int closestIndex = 0;

        // try all different spline points
        for (int i = 0; i < points.length; i++) {
            // get the distance from the spline point to the center of the entity
            float curDist = center.sub(points[i]).length();


            if (curDist < minDistance) {
                minDistance = curDist;
                closestIndex = i;
            }
        }

        return closestIndex;
    }

}
