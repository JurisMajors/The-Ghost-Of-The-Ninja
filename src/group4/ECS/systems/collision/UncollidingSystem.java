package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
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
        for (Entity other : cc.collisions) {
            if (other == e) continue;
            // get the displacement vector
            Vector3f colDim = processCollision(e, other);
            // if x and y collisions already happened break out
            mc.velocity.addi(colDim);
            // cap the velocity (for safety)
            mc.velocity.x = this.capDirection(mc.velocity.x, mc.velocityRange.x);
            mc.velocity.y = this.capDirection(mc.velocity.y, mc.velocityRange.y);
            // move in that direction
            curPos.addi(colDim);
        }

        // remove all collisions after fixing them all
        cc.collisions.clear();
    }

    /**
     * Produces a displacement vector for a possibly occuring collision
     *
     * @param first the entityto possibly move in case of collision
     * @param scnd  the other entity that is not moved
     * @return
     */
    Vector3f processCollision(Entity first, Entity scnd) {
        // get positions
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(scnd).position;
        // get the intersection rectangle
        Rectangle intersection = UncollidingSystem.getIntersectingRectangle(first, scnd);
        if (intersection == null) return new Vector3f();
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
     *
     * @param rectangle1 first rectangle
     * @param rectangle2 second rectangle
     * @return the rectangle which is common for rectangle1 and rectangle2
     * @throws IllegalArgumentException if pre is violated
     * @pre rectangle1.overlaps(rectangle2)
     */
    static public Rectangle intersect(Rectangle rectangle1, Rectangle rectangle2) throws IllegalArgumentException {
        if (!rectangle1.overlaps(rectangle2)) {
            throw new IllegalArgumentException("Attempted to get the intersecting rectangle from two non-intersecting rectangles. \n" +
                    "Rectangle 1: " + rectangle1.toString() + " \n" +
                    "Rectangle 2: " + rectangle2.toString());
        }
        // https://stackoverflow.com/questions/17267221/libgdx-get-intersection-rectangle-from-rectangle-overlaprectangle
        Rectangle intersection = new Rectangle();
        intersection.x = Math.max(rectangle1.x, rectangle2.x);
        intersection.width = Math.min(rectangle1.x + rectangle1.width, rectangle2.x + rectangle2.width) - intersection.x;
        intersection.y = Math.max(rectangle1.y, rectangle2.y);
        intersection.height = Math.min(rectangle1.y + rectangle1.height, rectangle2.y + rectangle2.height) - intersection.y;
        return intersection;
    }


    /**
     * Given information about a bounding boxes, determine whether they collide
     *
     * @param firstPos bottom left of first bb
     * @param firstDim dimensions of first bb
     * @param scndPos  bottom left of second bb
     * @param scndDim  dimensions of second bb
     * @return whether bounding boxes collide in XY
     */
    static public boolean collide(Vector3f firstPos, Vector3f firstDim,
                                  Vector3f scndPos, Vector3f scndDim) {
        // define bounding boxes
        Rectangle firstBB = bbAsRectangle(firstPos, firstDim);
        Rectangle scndBB = bbAsRectangle(scndPos, scndDim);
        // if doesnt overlap, no displacement
        return firstBB.overlaps(scndBB);
    }

    /**
     * Determines whether two collidable entities colide
     *
     * @return whether first collides with sceond
     */
    static public boolean collide(Entity first, Entity second) {
        // get positions
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(second).position;
        // get dimensions
        Vector3f firstDim = Mappers.dimensionMapper.get(first).dimension;
        Vector3f scndDim = Mappers.dimensionMapper.get(second).dimension;
        return UncollidingSystem.collide(firstPos, firstDim, scndPos, scndDim);
    }

    public static Rectangle getIntersectingRectangle(Entity first, Entity second) {
        if (!UncollidingSystem.collide(first, second)) {
            return null;
        }
        // get positions
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(second).position;
        // get dimensions
        Vector3f firstDim = Mappers.dimensionMapper.get(first).dimension;
        Vector3f scndDim = Mappers.dimensionMapper.get(second).dimension;
        return UncollidingSystem.intersect(bbAsRectangle(firstPos, firstDim),
                bbAsRectangle(scndPos, scndDim));
    }

    private float capDirection(float cur, float max) {
        if (max < 1e-6 && max > -1e-6) { // close to zero
            return 0f;
        }
        if (Math.abs(cur) < Math.abs(max)) { // no need to modify
            return cur;
        }
        // cap it
        return Math.abs(cur) / cur * Math.min(max, Math.abs(cur));
    }

    private static Rectangle bbAsRectangle(Vector3f botLeft, Vector3f dim) {
        return new Rectangle(botLeft.x, botLeft.y, dim.x, dim.y);
    }
}
