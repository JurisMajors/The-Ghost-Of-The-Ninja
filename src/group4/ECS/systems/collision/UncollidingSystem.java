package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;

/**
 * This applies collision to entities that can move and have a bounding box
 */
public class UncollidingSystem extends IteratingSystem {

    public UncollidingSystem(int priority) {
        // only process collisions for moving entities that are collidable
        super(Families.collidableMovingFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        uncollideEntity(entity, deltaTime, pc.position);
    }

    /**
     * Processes an entities collision and updates the position according to the collisions
     *
     * @param e         entity to process the collision with
     * @param deltaTime fps speed
     * @param curPos    the current position
     */
    void uncollideEntity(Entity e, float deltaTime, Vector3f curPos) {
        MovementComponent mc = Mappers.movementMapper.get(e);

        // get all entities that i collide with
        CollisionComponent cc = Mappers.collisionMapper.get(e);

        int resolved = 0; // keep track of resolved collisions

        for (CollisionData cd : cc.collisions) {
            // deal with splines
            if (cd.newPos != null) {
                // spline collision
                uncollideSpline(cd, mc, curPos);
            } else {
                // all normal collisions
                uncollideRectangle(cd, e, mc, resolved, curPos);
            }

            resolved++;
        }
    }

    /**
     * Uncollides a moving entity from a spline given the collision data.
     * Updates the mc and curPos of the moving entity.
     *
     * @param cd     collision data
     * @param mc     movement component of moving entity
     * @param curPos the current position of the moving entity
     */
    private void uncollideSpline(CollisionData cd, MovementComponent mc, Vector3f curPos) {
        if (cd.closestNormal.y < 0 && mc.velocity.y >= 0) {
            mc.velocity.y *= -0.5f;
        } else if (cd.closestNormal.y > 0 && mc.velocity.y <= 0) {
            mc.velocity.y = 0;

            // rotate velocity when walking over spline
            Vector3f up = new Vector3f(0, 0, 1.0f);
            Vector3f tangent = cd.closestNormal.cross(up);

            // move along the spline tangent with the speed that the x velocity was
            mc.velocity = tangent.scale(mc.velocity.x);
        }

        curPos.setVector(cd.newPos);
    }

    /**
     * Uncollides a moving entity (e) with a rectangle entity given the collision data.
     * Updates the mc and curPos of the moving entity (e).
     *
     * @param cd
     * @param e
     * @param mc
     * @param resolved
     * @param curPos
     */
    private void uncollideRectangle(CollisionData cd, Entity e, MovementComponent mc, int resolved, Vector3f curPos) {
        Entity other = cd.entity;
        if (other.equals(e)) return;

        // get the displacement vector
        Vector3f trueDisplacement;

        if (resolved != 0) { // if resolved more than one
            // recalculate the collision (since position has changed)
            trueDisplacement = CollisionSystem.processCollision(e, other);
        } else {
            trueDisplacement = cd.displacement;
        }

        handleVelocity(mc, trueDisplacement);
        // displace the positions
        curPos.addi(trueDisplacement);
    }

    /**
     * TODO: add javadoc
     * @param mc
     * @param displacement
     */
    private void handleVelocity (MovementComponent mc, Vector3f displacement) {
        if (displacement.y > 0) { // displacement from bottom
            if (mc.velocity.y <= 0) { // if falling down
                mc.velocity.y = 0; // set velocity to zero
            }
        } else if (displacement.y < 0){  // displacement from top
            mc.velocity.y *= -0.3; // go down when hit from top
        }

        if (displacement.x != 0) {
            mc.velocity.x = 0;
        }
        // cap the velocity (for safety)
        mc.velocity.capValuesi(mc.velocityRange);
    }

}
