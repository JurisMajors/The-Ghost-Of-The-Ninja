package group4.ECS.systems.movement.MovementHandlers;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.SplinePathComponent;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.IntersectionPair;
import group4.maths.Ray;
import group4.maths.Vector3f;
import group4.utils.DebugUtils;

import java.util.ArrayList;
import java.util.List;

import static group4.ECS.components.stats.MovementComponent.LEFT;
import static group4.ECS.components.stats.MovementComponent.RIGHT;


public abstract class AbstractMovementHandler<T extends Mob> extends IteratingSystem {


    public AbstractMovementHandler(Family family, int priority) {
        super(family, priority);
    }

    public void handleMovement(Entity entity, float deltaTime) {
        // Get some components from the mob that is supposed to move
        PositionComponent pc = Mappers.positionMapper.get(entity);
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent gc = Mappers.gravityMapper.get(entity);
        MobComponent mobComponent = Mappers.mobMapper.get(entity);

        // determine the range of vision this mob has, depends on if its 'chasing' or 'idle'
        setVisionRange(entity);

        boolean needsToMove = true;
        boolean canSeePlayer = canSeePlayer(pc, dc, mobComponent.currentVisionRange);

        // the position that this entity wants to move towards
        Vector3f targetPosition = null;

        // handle mobs that should follow a spline
        if (isSplineMob(entity)) {
            if (canSeePlayer) {
                // make sure that were not following a spline anymore
                if (isSplineMob(entity)) {
                    SplinePathComponent spc = Mappers.splinePathMapper.get(entity);
                    spc.leaveSpline();
                }
            } else {
                // move along the spline
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
            }
        }

        // determine the target position if it is not yet determined
        if (canSeePlayer) {
            // set target to the player position
            targetPosition = getPlayerPosition();
        } else {
            if (targetPosition == null) {
                // don't move
                needsToMove = false;
                targetPosition = pc.position;
            }
        }

        if (needsToMove) {
            // get velocity for mob
            move(entity, targetPosition, deltaTime);
        } else {
            becomeIdle(mc);
        }
        // apply gravity
        doGravity(mc, gc);

        // update position
        pc.position.addi(mc.velocity);
    }

    protected void move(Entity e, Vector3f targetPosition, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        // TODO: store specific states in different components
        HealthComponent hc = Mappers.healthMapper.get(e);

        if (hc == null || hc.state.contains(EntityConst.EntityState.KNOCKED)) {
            return;
        }

        // set velocity.x in the direction towards the player
        if (Math.abs(pc.position.x - targetPosition.x) <= Math.abs(mc.velocity.x)) {
            // if close enough, don't overshoot
            mc.velocity.x = targetPosition.x - pc.position.x;
        } else if (pc.position.x < targetPosition.x && canMoveRight(mc.velocity)) {
            moveRight(mc);
            mc.orientation = RIGHT;
        } else if (pc.position.x > targetPosition.x && canMoveLeft(mc.velocity)) {
            moveLeft(mc);
            mc.orientation = LEFT;
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

    protected void becomeIdle(MovementComponent mc) {
        mc.velocity.x = 0;
    }

    protected void doGravity(MovementComponent mc, GravityComponent gc) {
        mc.velocity.y -= gc.gravity.y;
    }

    protected Vector3f getPlayerPosition() {
        return Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0)).position;
    }

    private Vector3f getPlayerDimension() {
        return Mappers.dimensionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0)).dimension;
    }

    /**
     * Sets the vision range for the mob depending on if he is chasing the player or is idle.
     *
     * @param mob
     */
    protected void setVisionRange(Entity mob) {
        MobComponent mc = Mappers.mobMapper.get(mob);
        PositionComponent pc = Mappers.positionMapper.get(mob);
        DimensionComponent dc = Mappers.dimensionMapper.get(mob);

        if (canSeePlayer(pc, dc, mc.currentVisionRange)) {
            mc.currentVisionRange = MobComponent.chaseRange;
        } else {
            mc.currentVisionRange = MobComponent.viewRange;
        }
    }

    protected boolean canSeePlayer(PositionComponent pc, DimensionComponent dc, float viewRange) {
        List<Class<? extends Component>> seeThrough = new ArrayList<>();
        seeThrough.add(MobComponent.class);
        seeThrough.add(DamageComponent.class);
        seeThrough.add(GhostComponent.class);


        // center of the mob
        Vector3f center = pc.position.add(dc.dimension.scale(0.5f));

        // only cast rays when the player is close enough to the mob
        float distance = getPlayerPosition().euclidDist(center);

        if (distance > viewRange + 1) {
            return false;
        }

        // get the corners of the mob
        Vector3f[] mobCorners = getCorners(pc.position, dc.dimension);

        // the mob can see from all its corners and its center
        Vector3f[] mobEyes = new Vector3f[1];
        mobEyes[0] = center;
        /*
        // add the corners
        for (int i = 0; i < 4; i++) {
            mobEyes[i + 1] = mobCorners[i];
        }
         */

        // we want to look at all player corners
        Vector3f[] playerCorners = getCorners(getPlayerPosition(), getPlayerDimension());

        // cast rays for each corner combination to make sure that the mob detects the player if it has a ray that reaches the player
        for (int i = 0; i < mobEyes.length; i++) {
            for (int j = 0; j < playerCorners.length; j++) {
                // look from the eye to one of the player corners
                Vector3f dir = playerCorners[j].sub(mobEyes[i]);

                // cast the ray and get the point and entity it intersects with
                Ray ray = new Ray(center, dir, seeThrough, viewRange);
                IntersectionPair ip = ray.cast(TheEngine.getInstance().getEntitiesFor(Families.allCollidableFamily));

                DebugUtils.drawLine(mobEyes[i], ip.point);

                // if this ray reaches the player
                if (ip.entity instanceof Player && !(ip.entity instanceof Ghost)) {
                    return true;
                }
            }
        }

        // no ray hit the target
        return false;
    }

    /**
     * Returns the corners of a rectangle with bottom left corner position and dimension dimension.
     *
     * @param position
     * @param dimension
     * @return array of corner vectors
     */
    private Vector3f[] getCorners(Vector3f position, Vector3f dimension) {
        Vector3f[] result = new Vector3f[4];
        result[0] = position; // bl
        result[1] = position.add(new Vector3f(dimension.x, 0, 0)); // br
        result[2] = position.add(dimension); // tr
        result[3] = position.add(new Vector3f(0, dimension.y, 0)); // tl

        return result;
    }

    private final boolean isSplineMob(Entity e) {
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
