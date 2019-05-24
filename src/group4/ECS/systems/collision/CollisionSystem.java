package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import group4.ECS.components.SplineComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.entities.mobs.Mob;
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
        detectSplineCollisions(entity);
    }

    /**
     * Finds all non-spline collidable entities that collide with entity e. Stores them in the collision component and
     * lets the next collision system handle them.
     *
     * @param e entity
     */
    private void detectCollisions(Entity e) {

        // all relevant entities
        ImmutableArray<Entity> entities;

        // ???
        if (e instanceof Bullet) {
            entities = TheEngine.getInstance().getEntitiesFor(Families.allCollidableFamily);
        } else {
            entities = TheEngine.getInstance().getEntitiesFor(Families.collidableFamily);
        }

        // get all normal collidable entities
        CollisionComponent cc = Mappers.collisionMapper.get(e);
        for (Entity other : entities) {
            // dont process collision with itself
            if (e.equals(other)) continue;

            // dont register collisions bullets of bullets
            if (other instanceof Bullet && e instanceof Bullet) continue;
            if (e instanceof Bullet && (other instanceof Ghost || other instanceof Mob)) continue;
            if (other instanceof Player && e instanceof Player) continue;

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

    /**
     * Finds all spline collidable entities that collide with entity e. Stores them in the collision component and
     * lets the next collision system handle them.
     *
     * @param e entity
     */
    private void detectSplineCollisions(Entity e) {
        // get all collidable spline entities
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.collidableSplineFamily);
        CollisionComponent cc = Mappers.collisionMapper.get(e);

        PositionComponent pc = Mappers.positionMapper.get(e);
        DimensionComponent dc = Mappers.dimensionMapper.get(e);

        // get all corner points of the bounding box of the entity colliding with a spline
//        Vector3f ct = pc.position.add(dc.dimension.scale(0.5f));
        Vector3f bl = pc.position;
        Vector3f br = pc.position.add(new Vector3f(dc.dimension.x, 0.0f, dc.dimension.z));
        Vector3f tr = pc.position.add(dc.dimension);
        Vector3f tl = pc.position.add(new Vector3f(0.0f, dc.dimension.y, dc.dimension.z));

        for (Entity spline : entities) {
            if (e.equals(spline)) {
                continue;
            }
            PositionComponent spc = Mappers.positionMapper.get(spline);
            SplineComponent sc = Mappers.splineMapper.get(spline);

            // store the smallest vector that goes from a spline point to a boundingbox point
            Vector3f smallestDisplacement = new Vector3f(1000f, 1000f, 1000f);
            Vector3f closestPoint = null;
            Vector3f clostestNormal = null;

            // loop through all spline points
            for (int i = 0; i < sc.points.length; i++) {
                Vector3f point = sc.points[i];
                Vector3f normal = sc.normals[i];

                Vector3f oldSmallest = new Vector3f(smallestDisplacement);
                Vector3f worldPoint = point.add(spc.position);
                // compute difference with bounding box point and spline point and store the smallest displacement
                smallestDisplacement = Vector3f.min(smallestDisplacement, bl.sub(worldPoint));
                smallestDisplacement = Vector3f.min(smallestDisplacement, br.sub(worldPoint));
                smallestDisplacement = Vector3f.min(smallestDisplacement, tr.sub(worldPoint));
                smallestDisplacement = Vector3f.min(smallestDisplacement, tl.sub(worldPoint));
//                smallestDisplacement = Vector3f.min(smallestDisplacement, ct.sub(worldPoint));

                // get closest point to the bounding box
                if (smallestDisplacement.length() < oldSmallest.length()) {
                    closestPoint = point;
                    clostestNormal = normal;
                }
            }

            // make sure that the normal is facing the right way
            if (clostestNormal.scale(-1.0f).sub(smallestDisplacement).length() < clostestNormal.sub(smallestDisplacement).length()) {
                clostestNormal.scalei(-1.0f);
            }

            // get the position on the spline edge closest to the bb
            Vector3f newPos = closestPoint.add(clostestNormal);

            // if the smallest displacement is smaller than half of the thickness there is a collision
            // TODO: FIX HACK AT END OF IF STATEMENT VERY DANGAROUS
            if (smallestDisplacement.length() <= sc.normals[0].scale(0.5f * sc.thickness).length()) {
                smallestDisplacement.scalei(smallestDisplacement.sub(sc.normals[0].scale(0.5f * sc.thickness)).length());
                CollisionComponent scc = Mappers.collisionMapper.get(spline);

                // add collision to entity and spline
                CollisionData c1 = new CollisionData(spline, smallestDisplacement, newPos);
                CollisionData c2 = new CollisionData(e, smallestDisplacement.scale(-1.0f), newPos);
                cc.collisions.add(c1);
                scc.collisions.add(c2);
            }
        }
    }

    /// BELOW ARE FUNCTIONS TO DEAL WITH AXIS ALIGNED RECTANGLE INTERSECTION
    // TODO: maybe move those around a bit since there will also be functions to deal with spline intersection


    /**
     * Produces a displacement vector for a possibly occuring collision
     *
     * @param first the entity to possibly move in case of collision
     * @param scnd  the other entity that is not moved
     * @return
     */
    static Vector3f processCollision(Entity first, Entity scnd) {
        // intersection rectangle
        Rectangle intersection = getIntersectingRectangle(first, scnd);

        // if no overlap, do not displace
        if (intersection == null) return new Vector3f();

        // resolve the collision with the smallest displacement
        if (intersection.height <= intersection.width) {
            return resolveYCollision(first, scnd, intersection);
        }
        return resolveXCollision(first, scnd, intersection);
    }

    /**
     * TODO: add javadoc
     * @param first
     * @param scnd
     * @param intersection
     * @return
     */
    private static Vector3f resolveYCollision(Entity first, Entity scnd, Rectangle intersection) {
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(scnd).position;
        Vector3f firstDim = Mappers.dimensionMapper.get(first).dimension;
        Vector3f scndDim = Mappers.dimensionMapper.get(scnd).dimension;
        Vector3f firstCntr = firstPos.add(firstDim.scale(0.5f));

        // if the first is not on or below the second, then no displacement
        if (!(firstCntr.x >= scndPos.x && firstCntr.x <= scndPos.x + scndDim.x)) {
            return new Vector3f();
        }

        // otherwise displace in the inverse direction
        if (firstPos.y > scndPos.y) { // first is on top
            return new Vector3f(0, intersection.height, 0);
        } else { // on bottom
            return new Vector3f(0, -1 * intersection.height, 0);
        }
    }

    /**
     * TODO: add javadoc
     * @param first
     * @param scnd
     * @param intersection
     * @return
     */
    private static Vector3f resolveXCollision(Entity first, Entity scnd, Rectangle intersection) {
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(scnd).position;

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
     * Determines whether two collidable entities collide
     *
     * @return whether first collides with second
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

    /**
     * TODO: add lavadoc
     * @param first
     * @param second
     * @return
     */
    public static Rectangle getIntersectingRectangle(Entity first, Entity second) {
        // if entities don't collide
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

    /**
     * TODO: add javadoc
     * @param botLeft
     * @param dim
     * @return
     */
    private static Rectangle bbAsRectangle(Vector3f botLeft, Vector3f dim) {
        return new Rectangle(botLeft.x, botLeft.y, dim.x, dim.y);
    }
}
