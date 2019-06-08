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
import group4.ECS.entities.totems.Totem;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

/**
 * This applies collision to entities that can move and have a bounding box
 */
public class CollisionSystem extends IteratingSystem {

    public CollisionSystem(int priority) {
        // only process collisions for moving entities that are collidable
        super(Families.collidableMovingFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        setOnPlatform(entity, false);

        // detect and store all collisions
        detectCollisions(entity);
        detectSplineCollisions(entity);
    }

    private void setOnPlatform(Entity e, boolean bool) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        pc.onPlatform = bool;
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
            if (other instanceof Totem && !(e instanceof Player)) continue;

            // get the intersection between this (moving collidable entity) and other (collidable entity)
            Rectangle intersection = getIntersectingRectangle(e, other);

            // if there is no intersection, do nothing
            if (intersection == null) {
                continue;
            }

            // Get displacement vector
            Vector3f displacement = processCollision(e, other);

            if (displacement.y > 0) {
                setOnPlatform(e, true);
            }

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

        // entity position and dimension
        PositionComponent pc = Mappers.positionMapper.get(e);
        DimensionComponent dc = Mappers.dimensionMapper.get(e);


        // loop through all collidable splines
        for (Entity spline : entities) {
            // don't do collision with the same object
            if (e.equals(spline)) {
                continue;
            }

            // detect and process the collision with e and spline
            handleSpline(e, spline);
        }
    }

    /**
     * Handles collision with moving entity e and spline spline.
     *
     * @param e      moving entity
     * @param spline spline entity
     */
    private void handleSpline(Entity e, Entity spline) {
        // if there is no collision we do nothing
        if (!isInSpline(e, spline)) return;

        // spline components
        PositionComponent spc = Mappers.positionMapper.get(spline);
        SplineComponent sc = Mappers.splineMapper.get(spline);

        // (moving) entity components
        PositionComponent pc = Mappers.positionMapper.get(e);
        CollisionComponent cc = Mappers.collisionMapper.get(e);
        DimensionComponent dc = Mappers.dimensionMapper.get(e);

        // encode the corners of the moving entities rectangle (bl, br, tr, tl)
        Vector3f[] corners = getCorners(pc.position, dc.dimension);

        // for each corner store the spline point (and normal) closest to it
        Vector3f[] closestPoints = new Vector3f[corners.length];
        Vector3f[] closestNormals = new Vector3f[corners.length];

        // initialize the arrays with the first point/normal
        for (int k = 0; k < closestPoints.length; k++) {
            closestPoints[k] = sc.points[0];
            closestNormals[k] = sc.normals[0];
        }

        // get the center of the moving entity
        Vector3f center = corners[0].add(dc.dimension.scale(0.5f));

        // find the closest spline point for each corner and store it in closestPoints (and closestNormals)
        findClosestPoints(sc, spc, corners, closestPoints, closestNormals, center);

        // make sure that all normals are pointing the right direction
        flipNormals(center, closestPoints, closestNormals);

        // this is a mask to discard certain corners from the collision process
        boolean[] discard = getDiscardedCorners(sc.thickness, corners, closestPoints);

        // store the point on the spline that is closest to the closest corner (and its normal)
        Vector3f closestPoint = new Vector3f();
        Vector3f closestNormal = new Vector3f();
        int smallestCode = getClosestPoint(corners, closestPoints, closestNormals, discard, closestPoint, closestNormal);

        // manage on platform
        if (closestNormal.y > 0) {
            setOnPlatform(e, true);
        }

        // the target corner should be moved towards this position (on the edge of the spline)
        Vector3f newPos = closestPoint.add(closestNormal.scale(0.5f * sc.thickness));

        // correct for which corner needs to get this new position
        cornerCorrection(newPos, dc.dimension, smallestCode);

        CollisionComponent scc = Mappers.collisionMapper.get(spline);

        // add collision to entity and spline
        CollisionData c1 = new CollisionData(spline, closestNormal, newPos);
        CollisionData c2 = new CollisionData(e, closestNormal.scale(-1.0f), newPos);
        cc.collisions.add(c1);
        scc.collisions.add(c2);
    }

    /**
     * Given a list of corners and their closest spline points, and a discard array mask, set the final closestPoint
     * and closestNormal of the moving entity and the spline. Return the index or (cornder-)code for the
     * eventual corner that becomes the closest point.
     * @param corners corners of the entity
     * @param closestPoints closest point on spline for each corner
     * @param closestNormals normal for the corresponding closest spline point
     * @param discard boolean mask
     * @param closestPoint closest point which gets set in this function
     * @param closestNormal closest normal which gets set in this function
     * @return code/index for the corner that will get displaced and is closest to the spline
     */
    private int getClosestPoint(Vector3f[] corners, Vector3f[] closestPoints, Vector3f[] closestNormals, boolean[] discard, Vector3f closestPoint, Vector3f closestNormal) {
        int smallestCode = -1;

        // distance of the closest spline point towards any corner
        float minLen = Float.MAX_VALUE;

        for (int k = 0; k < closestPoints.length; k++) {
            // ignore discarded corners
            if (discard[k]) continue;

            // update the closest point and code if the distance is smaller
            if (corners[k].euclidDist(closestPoints[k]) < minLen) {
                minLen = corners[k].sub(closestPoints[k]).length();
                closestPoint.setVector(closestPoints[k]);

                closestNormal.setVector(closestNormals[k]);
                smallestCode = k;
            }
        }

        return smallestCode;
    }

    /**
     * Discard a corner if it is not inside the spline platform.
     *
     * @param thickness     thickness of the platform (from top to bottom)
     * @param corners       corners that we are masking
     * @param closestPoints closest point on spline for each corner
     * @return mask of true and false, true for corners that need to be discarded
     */
    private boolean[] getDiscardedCorners(float thickness, Vector3f[] corners, Vector3f[] closestPoints) {
        boolean[] result = new boolean[closestPoints.length];

        // if the corner is too far away we discard it
        for (int k = 0; k < closestPoints.length; k++) {
            if (corners[k].sub(closestPoints[k]).length() > 0.5f * thickness) {
                result[k] = true;
            }
        }
        return result;
    }

    /**
     * Flip the normals upside down if the (center of the) entity is below the spline.
     *
     * @param center         center of the entity
     * @param closestPoints
     * @param closestNormals
     */
    private void flipNormals(Vector3f center, Vector3f[] closestPoints, Vector3f[] closestNormals) {
        for (int k = 0; k < closestNormals.length; k++) {

            // flip the normal if the center is below the spline
            Vector3f centerDir = center.sub(closestPoints[k]);
            boolean flip = centerDir.y < 0;

            if (flip) {
                closestNormals[k].scalei(-1.0f);
            }

        }
    }

    /**
     * Finds the closest spline point to all corners inside the corners array. For each corners[i] the closest point
     * gets stored inside closestPoints[i] and the corresponding normal gets stored inside closestNormals[i].
     *
     * @param sc             spline component of the spline
     * @param spc            position component of the spline
     * @param corners        corners of the moving entity
     * @param closestPoints  vector3f array with same size as corners
     * @param closestNormals vector3f array with same size as corners
     */
    private void findClosestPoints(SplineComponent sc, PositionComponent spc, Vector3f[] corners, Vector3f[] closestPoints, Vector3f[] closestNormals, Vector3f center) {
        // get the closest points
        for (int i = 0; i < sc.points.length; i++) {
            // get local spline position and its normal
            Vector3f point = sc.points[i];
            Vector3f normal = sc.normals[i];

            // magic
            Vector3f oppositeNormal = sc.normals[sc.points.length - i - 1];

            // change the spline coordinates to world space
            Vector3f worldPoint = point.add(spc.position);

            // for each corner update the closest point and normal
            for (int k = 0; k < 4; k++) {
                if (corners[k].euclidDist(worldPoint) < corners[k].euclidDist(closestPoints[k])) {
                    closestPoints[k] = new Vector3f(worldPoint);

                    // get the normal in some magical way
                    boolean isBelow = center.sub(closestPoints[k]).y < 0;
                    if (isBelow) {
                        closestNormals[k] = new Vector3f(oppositeNormal);
                    } else {
                        closestNormals[k] = new Vector3f(normal);
                    }

                }
            }
        }
    }

    /**
     * Returns true if the intersection between e and spline is not empty.
     *
     * @param e
     * @param spline
     * @return boolean
     */
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
