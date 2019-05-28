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

    public UncollidingSystem() {
        // only process collisions for moving entities that are collidable
        super(Families.collidableMovingFamily);
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
        PositionComponent pc = Mappers.positionMapper.get(e);
        // get all entities that i collide with
        CollisionComponent cc = Mappers.collisionMapper.get(e);
        int resolved = 0; // keep track of resolved collisions
        for (CollisionData cd : cc.collisions) {
            // if there is a new position component in the collision it means its a spline collision and
            // the new position of this entity should be at newpos
            if (cd.newPos != null) {

                System.out.println(cd.closestNormal);
                if (cd.closestNormal.y < 0) {
                    mc.velocity.y = -0.5f * mc.velocity.y;
                } else {
                    mc.velocity = new Vector3f();
                }

                pc.position = cd.newPos;
                continue;
            }

            // all normal collisions
            Entity other = cd.entity;
            if (other.equals(e)) continue;
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
            resolved ++;
        }

        // remove all collisions after fixing them all
        cc.collisions.clear();
    }

    private void handleVelocity(MovementComponent mc, Vector3f displacement) {
        if (displacement.y > 0) { // displacement from bottom
            if (mc.velocity.y <= 0) { // if falling down
                mc.velocity.y = 0; // set velocity to zero
            }
        } else if (displacement.y < 0) {  // displacement from top
            mc.velocity.y *= -0.5; // go down when hit from top
        }

        if (displacement.x != 0) {
            mc.velocity.x = 0;
        }
        // cap the velocity (for safety)
        mc.velocity.capValuesi(mc.velocityRange);

    }

}
