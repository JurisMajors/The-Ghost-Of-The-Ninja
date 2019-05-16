package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.CollisionComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
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
        // get all entities that i collide with
        CollisionComponent cc = Mappers.collisionMapper.get(e);

        for (CollisionData cd : cc.collisions) {
            Entity other = cd.entity;
            if (other.equals(e)) continue;
            // get the displacement vector
            Vector3f colDim = cd.displacement;
            handleVelocity(mc, colDim);

            // displace the position
            curPos.addi(colDim);
        }

        // remove all collisions after fixing them all
        cc.collisions.clear();
    }

    private void handleVelocity (MovementComponent mc, Vector3f displacement) {
        if (displacement.y > 0) { // displacement from bottom
            if (mc.velocity.y <= 0) { // if falling down
                mc.velocity.y = 0; // set velocity to zero
            }
        } else if (displacement.y < 0){  // displacement from top
            mc.velocity.y += displacement.y; // go down when hit from top
        }

        if (displacement.x != 0) {
            mc.velocity.x = 0;
        }
        // cap the velocity (for safety)
        mc.velocity.capValuesi(mc.velocityRange);

    }






}
