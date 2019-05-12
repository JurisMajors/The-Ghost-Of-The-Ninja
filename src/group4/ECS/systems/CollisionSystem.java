package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

/**
 * This applies collision to entities that can move and have a bounding box
 */
public class CollisionSystem extends IteratingSystem {

    public CollisionSystem() {
        super(Families.collidableMovingFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);

        uncollideEntity(entity, deltaTime, pc.position);

    }

    /**
     * Processes an entities collision and updates the position according to the collisions
     * @param e entity to process the collision with
     * @param deltaTime fps speed
     * @param curPos the current position
     */
    void uncollideEntity(Entity e, float deltaTime, Vector3f curPos) {
        MovementComponent mc = Mappers.movementMapper.get(e);
        // get all entities that i can collide with
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.collidableFamily);
        for (Entity other : entities) {
            if (other == e) continue;
            // get the displacement vector
            Vector3f colDim = processCollision(e, other);
            // if x and y collisions already happened break out
            mc.velocity.addi(colDim);
            // cap the velocity (for safety)
            mc.velocity.x = this.capDirection(mc.velocity.x, mc.velocityRange.x);
            mc.velocity.y = this.capDirection(mc.velocity.y, mc.velocityRange.y);
            // move in that direction
            curPos.addi(colDim.scale(deltaTime));
        }
    }

    /**
     * Produces a displacement vector for a possibly occuring collision
     * @param first the entityto possibly move in case of collision
     * @param scnd the other entity that is not moved
     * @return
     */
    Vector3f processCollision(Entity first, Entity scnd) {
        // get positions
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(scnd).position;
        // get dimensions
        Vector3f firstDim = Mappers.dimensionMapper.get(first).dimension;
        Vector3f scndDim = Mappers.dimensionMapper.get(scnd).dimension;
        // define bounding boxes
        Rectangle firstBB = new Rectangle(firstPos.x, firstPos.y, firstDim.x, firstDim.y);
        Rectangle scndBB = new Rectangle(scndPos.x, scndPos.y, scndDim.x, scndDim.y);
        // if doesnt overlap, no displacement
        if (!firstBB.overlaps(scndBB)) return new Vector3f();
        // otherwise they collide..
        // get the intersection rectangle
        Rectangle intersection = intersect(firstBB, scndBB);
        // displace according to the rectangle
        if (intersection.height <= intersection.width) { // TODO: consider y collision from top
            return new Vector3f(0, intersection.height, 0);
        }
        // move in the correct x direction
        if (firstPos.x > scndPos.x) {
            return new Vector3f(intersection.width, 0, 0);
        }
        return new Vector3f(-1 * intersection.width, 0, 0);
    }


    /**
     * Produces an rectangle representing the intersecting area
     * @param rectangle1 first rectangle
     * @param rectangle2 second rectangle
     * @pre rectangle1.overlaps(rectangle2)
     * @return the rectangle which is common for rectangle1 and rectangle2
     */
    static public Rectangle intersect(Rectangle  rectangle1, Rectangle rectangle2 ) {
        // https://stackoverflow.com/questions/17267221/libgdx-get-intersection-rectangle-from-rectangle-overlaprectangle
        Rectangle intersection = new Rectangle();
        intersection.x = Math.max(rectangle1.x, rectangle2.x);
        intersection.width = Math.min(rectangle1.x + rectangle1.width, rectangle2.x + rectangle2.width) - intersection.x;
        intersection.y = Math.max(rectangle1.y, rectangle2.y);
        intersection.height = Math.min(rectangle1.y + rectangle1.height, rectangle2.y + rectangle2.height) - intersection.y;
        return intersection;
    }

    private float capDirection (float cur, float max) {
         return Math.abs(cur) / cur * Math.min(max, Math.abs(cur));
    }
}
