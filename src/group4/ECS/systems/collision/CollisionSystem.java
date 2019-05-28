package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.BooleanArray;
import group4.ECS.components.SplineComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;
import group4.utils.DebugUtils;

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
        ImmutableArray<Entity> entities;
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

        // encode the corners of the rectangle (bl, br, tr, tl)
        int[] codes = new int[]{0, 1, 2, 3};
        Vector3f[] corners = getCorners(pc.position, dc.dimension);

        // loop through all collidable splines
        for (Entity spline : entities) {
            // don't do collision with the same object
            if (e.equals(spline)) {
                continue;
            }

            handleSpline(e, spline, codes, corners, cc, dc);
        }
    }

    /**
     * Handles collision with moving entity e and spline spline.
     *
     * @param e       moving entity
     * @param spline  spline entity
     * @param codes   corner encoding
     * @param corners corners of e
     * @param cc      e's CollisionComponent
     * @param dc      e's DimensionComponent
     */
    private void handleSpline(Entity e, Entity spline, int[] codes, Vector3f[] corners, CollisionComponent cc, DimensionComponent dc) {

        // (moving) entity components
        MovementComponent mc = Mappers.movementMapper.get(e);
        // TODO: get other components this way

        // spline components
        PositionComponent spc = Mappers.positionMapper.get(spline);
        SplineComponent sc = Mappers.splineMapper.get(spline);

        // for each corner store the spline point (and normal) closest to it
        Vector3f[] closestPoints = new Vector3f[corners.length];
        Vector3f[] closestNormals = new Vector3f[corners.length];

        // initialize the arrays with the first point/normal
        for (int k = 0; k < closestPoints.length; k++) {
            closestPoints[k] = sc.points[0];
            closestNormals[k] = sc.normals[0];
        }

        // store the smallest vector that goes from a spline point to one of the points in the bounding box
//        Vector3f smallestDisplacement = new Vector3f(Float.MAX_VALUE, 0f, 0f);

        // store the code for the corresponding corner
        int smallestCode = -1;

        // store the point on the spline that is closest to the closest bounding box point
        Vector3f closestPoint = null;
        Vector3f closestNormal = null;

        for (int i = 0; i < sc.points.length; i++) {
            // get local spline position and its normal
            Vector3f point = sc.points[i];
            Vector3f normal = sc.normals[i];
//            Vector3f oldSmallest = new Vector3f(smallestDisplacement);

            // change the spline coordinates to world space
            Vector3f worldPoint = point.add(spc.position);

            // for each corner update the closest point and normal
            for (int k = 0; k < 4; k++) {
                if (corners[k].sub(worldPoint).length() < corners[k].sub(closestPoints[k]).length()) {
                    closestPoints[k] = new Vector3f(worldPoint);
                    closestNormals[k] = new Vector3f(normal);
                }

                // TODO: remove
//                if (corners[k].sub(worldPoint).length() < smallestDisplacement.length()) {
//                    smallestDisplacement = corners[k].sub(worldPoint);
//                    smallestCode = codes[k];
//                }
            }

            // TODO: remove
            // get closest point to the bounding box
//            if (smallestDisplacement.length() < oldSmallest.length()) {
//                closestPoint = worldPoint;
//                closestNormal = normal;
//            }
        }

        // make sure that all normals are pointing the right direction
        for (int k = 0; k < closestNormals.length; k++) {
            // vector from closest spline point to corner
            Vector3f displacement = corners[k].sub(closestPoints[k]);

            Vector3f center = corners[0].add(dc.dimension.scale(0.5f));
            Vector3f centerDir = center.sub(closestPoints[k]);
            boolean flip = centerDir.y < 0;
//            if (closestNormals[k].scale(-1.0f).sub(displacement).length() < closestNormals[k].sub(displacement).length()) {
            if (flip) {
                closestNormals[k].scalei(-1.0f);
            }

            /*
            // angle between the normal and the displacement
            float angle = displacement.angle(closestNormals[k]);
//            System.out.println(angle);

            // if the normal is pointing the other way, flip it so that it is pointing the same way as the displacement
            if (angle > 90) {
                closestNormals[k].scalei(-1.0f);
            }
             */
        }

        boolean[] discard = new boolean[closestPoints.length];

        Vector3f velocity = mc.velocity;

        for (int k = 0; k < closestPoints.length; k++) {
            if (corners[k].sub(closestPoints[k]).angle(velocity) < 90) {
//                discard[k] = true;
            }
            if (corners[k].sub(closestPoints[k]).length() > 0.5f * sc.thickness) {
                discard[k] = true;
            }
        }

        boolean allTrue = true;
        for (int k = 0; k < discard.length; k++) {
            if (!discard[k]) {
                allTrue = false;
                break;
            }
        }

        if (allTrue) {
            // no collision
        } else {
            float minLen = Float.MAX_VALUE;
            for (int k = 0; k < closestPoints.length; k++) {
                if (discard[k]) continue;

                if (corners[k].sub(closestPoints[k]).length() < minLen) {
                    minLen = corners[k].sub(closestPoints[k]).length();
                    closestPoint = closestPoints[k];
                    closestNormal = closestNormals[k];
                    smallestCode = k;
                }
            }

            DebugUtils.setColor(new Vector3f(0, 1.0f, 0));
            DebugUtils.drawBox(closestPoint, closestPoint.add(new Vector3f(0.1f, 0.1f, 0.1f)));
            DebugUtils.drawLine(closestPoint, closestPoint.add(closestNormal.scale(0.5f * sc.thickness)));

            DebugUtils.setColor(new Vector3f(1.0f, 0, 0));
            Vector3f newPos = closestPoint.add(closestNormal.scale(0.5f * sc.thickness));

            // correct for which corner needs to get this new position
            cornerCorrection(newPos, dc.dimension, smallestCode);

            // if the smallest displacement is smaller than half of the thickness there is a collision
//            if (smallestDisplacement.length() <= 0.5f * sc.thickness) {
            CollisionComponent scc = Mappers.collisionMapper.get(spline);

            // add collision to entity and spline
            CollisionData c1 = new CollisionData(spline, closestNormal, newPos);
            CollisionData c2 = new CollisionData(e, closestNormal.scale(-1.0f), newPos);
            cc.collisions.add(c1);
            scc.collisions.add(c2);
//            }
        }

//        for (int k = 0; k < closestNormals.length; k++) {
//            System.out.println(k + ": " + closestNormals[k]);
//        }


        // TODO: already done in new thing
        // make sure that the normal is facing the right way
//        if (clostestNormal.scale(-1.0f).sub(smallestDisplacement).length() < clostestNormal.sub(smallestDisplacement).length()) {
//            clostestNormal.scalei(-1.0f);
//        }

        // get the position on the spline edge closest to the bounding box

    }

    private boolean isInSpline(Entity e, Entity spline) {
        // entity components
        PositionComponent pc = Mappers.positionMapper.get(e);
        DimensionComponent dc = Mappers.dimensionMapper.get(e);
        Vector3f[] corners = getCorners(pc.position, dc.dimension);

        // spline components
        PositionComponent spc = Mappers.positionMapper.get(spline);
        SplineComponent sc = Mappers.splineMapper.get(spline);

        for (int i = 0; i < sc.points.length; i++) { // loop over spline points
            Vector3f worldPoint = sc.points[i].add(spc.position);
            for (int j = 0; j < corners.length; j++) { // loop over all corners
                // if this corner is inside the spline return true
                if (worldPoint.sub(corners[j]).length() < sc.normals[i].scale(0.5f * sc.thickness).length()) {
                    DebugUtils.drawBox(corners[j], corners[j].add(new Vector3f(0.1f, 0.1f, 0.1f)));
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Corrects the new position for which corner it is for.
     * The newPosition is for the corner represented by smallestCode.
     * After this function is called newPos is for the bottomLeft postion.
     *
     * @param newPos       new position for a corner
     * @param dimension    dimension of the rectangle
     * @param smallestCode code for the corner
     */
    private void cornerCorrection(Vector3f newPos, Vector3f dimension, int smallestCode) {
        // do a position offset according to which corner of the bounding box is on the spline
        if (smallestCode == 1) {
            // bottom right corner needs to be shifted left by the width
            newPos.x -= dimension.x;
        } else if (smallestCode == 2) {
            // top right corner needs to be shifted left and up by width and down by height
            newPos.x -= dimension.x;
            newPos.y -= dimension.y;
        } else if (smallestCode == 3) {
            // top left corner needs to be shifted down by height
            newPos.y -= dimension.y;
        }

    }

    /**
     * Given a position and dimension, return a list of the corner of the rectangle represented
     * by this position and dimension. The order is
     * {BottomLeft, BottomRight, TopRight, TopLeft}.
     *
     * @param position
     * @param dimension
     * @return corners of rectangle
     */
    private Vector3f[] getCorners(Vector3f position, Vector3f dimension) {
        Vector3f[] result = new Vector3f[4];

        // get all corner points of the bounding box of the entity colliding with a spline
        result[0] = position; // bottom left
        result[1] = position.add(new Vector3f(dimension.x, 0.0f, dimension.z)); // bottom right
        result[2] = position.add(dimension); // top right
        result[3] = position.add(new Vector3f(0.0f, dimension.y, dimension.z)); // top left

        return result;
    }

    /// BELOW ARE FUNCTIONS TO DEAL WITH AXIS ALIGNED RECTANGLE INTERSECTION

    /**
     * Produces a displacement vector for a possibly occuring collision
     *
     * @param first the entityto possibly move in case of collision
     * @param scnd  the other entity that is not moved
     * @return
     */
    static Vector3f processCollision(Entity first, Entity scnd) {
        Rectangle intersection = getIntersectingRectangle(first, scnd);
        if (intersection == null) return new Vector3f();
        // resolve the collision with the smallest displacement
        if (intersection.height <= intersection.width) {
            return resolveYCollision(first, scnd, intersection);

        }
        return resolveXCollision(first, scnd, intersection);
    }

    private static Vector3f resolveYCollision(Entity first, Entity scnd, Rectangle intersection) {
        Vector3f firstPos = Mappers.positionMapper.get(first).position;
        Vector3f scndPos = Mappers.positionMapper.get(scnd).position;
        Vector3f firstDim = Mappers.dimensionMapper.get(first).dimension;
        Vector3f scndDim = Mappers.dimensionMapper.get(scnd).dimension;
        Vector3f firstCntr = firstPos.add(firstDim.scale(0.5f));

        // if the first is not on or below the second, then no displacement
        if (!(firstCntr.x + 0.3f >= scndPos.x && firstCntr.x - 0.3f <= scndPos.x + scndDim.x)) {
            return new Vector3f();
        }
        // otherwise displace in the inverse direction
        if (firstPos.y > scndPos.y) { // first is on top
            return new Vector3f(0, intersection.height, 0);
        } else { // on bottom
            return new Vector3f(0, -1 * intersection.height, 0);
        }
    }

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
