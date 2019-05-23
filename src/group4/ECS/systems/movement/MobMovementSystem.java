package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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
        // send rays and check if the mob can see the player
        if (canSeePlayer(360, 36, pc, dc, mc)) {
            // get the player position
            playerPos = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        } else if (isSplineMob(entity)) {
            // splines mobs have a different target than normal mobs
            SplinePathComponent spc = Mappers.splinePathMapper.get(entity);
            // get the direction and position to move towards
            Vector3f targetDirection = targetDirection(spc.points, pc.position, dc.dimension, mc.velocity);
            Vector3f targetPos = pc.position.add(targetDirection);

            // the target position is currently stored in playerPos, this name should probably change
            playerPos = new PositionComponent(targetPos);
        } else {
            playerPos = new PositionComponent(pc.position);
        }

        // process movement events
        move(entity, playerPos, deltaTime);
        // apply gravity
        doGravity(mc, gc);

        pc.position.addi(mc.velocity.scale(deltaTime));
        /*
        This comment is a failed attempt to smooth out the movement of a flying mob trying to follow a spline
        // store the new and old distance to the target (playerPos)
        float curDist = pc.position.sub(playerPos.position).length();
        float newDist = pc.position.add(mc.velocity.scale(deltaTime)).sub(playerPos.position).length();
        // make sure to not overshoot to the target
        if (newDist > curDist - 1e-2) {
            // move on top of the target
            pc.position = new Vector3f(playerPos.position);
            // reset the velocity
            mc.velocity = new Vector3f();
        } else {
            // move in the specified direction
            pc.position.addi(mc.velocity.scale(deltaTime));
        }
         */
    }

    protected void move(Entity e, PositionComponent playerPos, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        float EPS = 0.05f;

        // set velocity.x in the direction towards the player
        if (Math.abs(pc.position.x - playerPos.position.x) <= Math.abs(mc.velocity.x)) {
            // if close enough, don't overshoot
            mc.velocity.x = playerPos.position.x - pc.position.x;
        } else if (pc.position.x < playerPos.position.x && canMoveRight(mc.velocity)) {
            moveRight(mc);
        } else if (pc.position.x > playerPos.position.x && canMoveLeft(mc.velocity)) {
            moveLeft(mc);
        } else {
            mc.velocity.x = 0;
        }

        // set velocity.y in the direction towards the player
        if (Math.abs(pc.position.y - playerPos.position.y) < Math.abs(mc.velocity.y)) {
            // if close enough, don't overshoot
            mc.velocity.y = playerPos.position.y - pc.position.y;
        } else if (pc.position.y < playerPos.position.y && canJump(mc.velocity)) {
            jump(mc);
        } else if (pc.position.y > playerPos.position.y && canMoveDown()) {
            moveDown(mc);
        } else if (canJump(mc.velocity) && canMoveDown()) {
            mc.velocity.y = 0;
        }

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

    /**
     * Gets the direction for a given entity to move towards if it wants to move towards the spline.
     *
     * @param points          spline approximation points
     * @param entityPosition  bl position of the entity
     * @param entityDimension dimension of the entity
     * @param entityVelocity  velocity of the entity
     * @return
     */
    public Vector3f targetDirection(Vector3f[] points, Vector3f entityPosition, Vector3f entityDimension, Vector3f entityVelocity) {
        Vector3f targetDirection = null;
        int offset = 5;

        Vector3f closestPoint = getClosestSplinePoint(points, entityPosition, entityDimension);
        System.out.println(closestPoint);
        float distance = entityPosition.sub(closestPoint).length();

        // if the entity is almost on the spline, follow the spline path
        if (distance < 0.5f) {
            // get the next and previous point on the spline
            int index = getClosestSplinePointIndex(points, entityPosition, entityDimension);
            Vector3f nextPoint = points[(index + 5) % points.length];
            Vector3f prevPoint = points[(index - 5 + points.length) % points.length];

            // get the corresponding directions
            Vector3f forwardDirection = nextPoint.sub(entityPosition).normalized();
            Vector3f backwardDirection = prevPoint.sub(entityPosition).normalized();

            // set the target direction the direction that is closest to the current velocity
            if (entityVelocity.sub(forwardDirection).length() < entityVelocity.sub(backwardDirection).length()) {
                targetDirection = forwardDirection;
            } else {
                targetDirection = backwardDirection;
            }
        } else { // otherwise, just move towards the spline
            targetDirection = closestPoint.sub(entityPosition).normalized();
        }

        // TODO: find a way to not share around the targetDirection too much

        // TODO: this approach doesn't work well enough
        if (Math.abs(targetDirection.y) < 0.1f) {
            targetDirection.y = 0;
        }

        // TODO: this approach also doesn't work well enough
        float EPS = 0.000001f;
        if (targetDirection.sub(entityVelocity.normalized()).length() < EPS) {
            targetDirection = new Vector3f(entityVelocity);
        }

        return targetDirection;
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
