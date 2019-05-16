package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.items.weapons.Bullet;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

/**
 * This applies collision to entities that can move and have a bounding box
 */
public class CollisionSystem extends IteratingSystem {

    public CollisionSystem() {
        // only process collisions for moving entities that are collidable
        super(Families.collidableMovingFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // detect and store all collisions
        detectCollisions(entity);
    }

    /**
     * Finds all collidable entities that collide with entity e. Stores them in the collision component and
     * lets the next collision system handle them.
     *
     * @param e entity
     */
    private void detectCollisions(Entity e) {
        // get all collidable entities
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.collidableFamily);
        CollisionComponent cc = Mappers.collisionMapper.get(e);

        for (Entity other : entities) {
            // dont process collision with itself
            if (e.equals(other)) continue;
            // dont register collisions bullets of bullets
            if (other instanceof Bullet && e instanceof Bullet) continue;

            // get the intersection between this (moving collidable entity) and other (collidable entity)
            Rectangle intersection = getIntersectingRectangle(e, other);

            // if there is no intersection, do nothing
            if (intersection == null) {
                continue;
            }

            // Get displacement vector
            Vector3f displacement = processCollision(e, other);

            CollisionComponent occ = Mappers.collisionMapper.get(other);
            // add the collision to both entities
            CollisionData c1 = new CollisionData(other, displacement);
            CollisionData c2 = new CollisionData(e, displacement.scale(-1.0f));
            cc.collisions.add(c1);
            occ.collisions.add(c2);
        }

    }

    /// BELOW ARE FUNCTIONS TO DEAL WITH AXIS ALIGNED RECTANGLE INTERSECTION
    // TODO: maybe move those around a bit since there will also be functions to deal with spline intersection


    /**
     * Produces a displacement vector for a possibly occuring collision
     *
     * @param first the entityto possibly move in case of collision
     * @param scnd  the other entity that is not moved
     * @return
     */
    static Vector3f processCollision(Entity first, Entity scnd) {
        // get positions
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(scnd).position;
        // get the intersection rectangle
        Rectangle intersection = getIntersectingRectangle(first, scnd);
        if (intersection == null) return new Vector3f();
        // displace according to the rectangles smallest side
        if (intersection.height <= intersection.width) {
            // inversely displace
            if (firstPos.y > scndPos.y) {
                return new Vector3f(0, intersection.height, 0);
            } else {
                return new Vector3f(0, -1* intersection.height, 0);
            }
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
        return collide(firstPos, firstDim, scndPos, scndDim);
    }

    public static Rectangle getIntersectingRectangle(Entity first, Entity second) {
        if (!collide(first, second)) {
            return null;
        }
        // get positions
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(second).position;
        // get dimensions
        Vector3f firstDim = Mappers.dimensionMapper.get(first).dimension;
        Vector3f scndDim = Mappers.dimensionMapper.get(second).dimension;
        return intersect(bbAsRectangle(firstPos, firstDim),
                bbAsRectangle(scndPos, scndDim));
    }

    private static Rectangle bbAsRectangle(Vector3f botLeft, Vector3f dim) {
        return new Rectangle(botLeft.x, botLeft.y, dim.x, dim.y);
    }
}
