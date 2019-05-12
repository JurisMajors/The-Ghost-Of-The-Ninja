package group4.maths;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GhostComponent;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.PlayerComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Mappers;

import java.util.ArrayList;
import java.util.List;

public class Ray {
    /** starting position of the ray **/
    Vector3f startPos;
    /** direction of the ray **/
    Vector3f dir;
    /** the end of the ray **/
    Vector3f end;

    public Ray(Vector3f startingPos, Vector3f dir) {
        this.startPos = startingPos;
        this.dir = dir;
        // TODO have smarter scaling to end point
        this.end = this.startPos.add(this.dir.normalized().scale(10000));
    }

    /**
     * Cast the ray to the given entities
     * @param entities the entities that the ray might intersect
     * @return position at first intersection of a bounding box
     */
    public IntersectionPair cast(ImmutableArray<Entity> entities) {
        Vector3f closestIntersection = null; // current closest intersection of the ray
        Entity intersectedEntity = null; // entity twhose bounding box is intersected
        float curDist = Float.MAX_VALUE; // the distance to the intersection

        // for each entity calculate the intersection
        for (Entity e : entities) {
            // TODO this does not generalize in case of CGI, maybe skip according to parameter?
            if (e.getComponent(GhostComponent.class) != null) continue; // skip the ghost
            if (e.getComponent(PlayerComponent.class) != null) continue; // skip the player

            List<Vector3f> intersections = this.intersects(e); // get intersection points

            // get minimal intersection point and update if necessary
            for (Vector3f inter : intersections) {
                // calculate intersections distance
                float interDist = inter.euclidDist(this.startPos);
                // update the closest intersection and distance if necessary;
                if (interDist < curDist) {
                    curDist = interDist;
                    closestIntersection = inter;
                    intersectedEntity = e;
                }
            }
        }

        return new IntersectionPair(closestIntersection, intersectedEntity);
    }

    /**
     *  Ray intersection point with a single entity
     * @param entity the entity to check for an intersection
     * @return the positions of intersections with the  bounding box of the entity
     */
    public List<Vector3f> intersects(Entity entity) {
        List<Vector3f> intersections = new ArrayList<>();
        PositionComponent posComp = Mappers.positionMapper.get(entity);
        DimensionComponent dimComp = Mappers.dimensionMapper.get(entity);
        Vector3f dim = dimComp.dimension;
        Vector3f botL = posComp.position;

        // points of the bounding box
        // the z coordinates dont matter, since we only care about 2D intersection
        Vector3f botLeft = new Vector3f(botL); // copy botleft
        Vector3f topLeft = new Vector3f(botLeft.x, botLeft.y + dim.y, botLeft.z);
        Vector3f topRight = botLeft.add(dim);
        Vector3f botRight = new Vector3f(botLeft.x + dim.x, botLeft.y, botLeft.z);

        // define lines of the bounding box
        List<List<Vector3f>> lines = new ArrayList<>();
        lines.add(createLine(topLeft, topRight));
        lines.add(createLine(topLeft, botLeft));
        lines.add(createLine(botLeft, botRight));
        lines.add(createLine(botRight, topRight));
        // this can be extended for more dimensions if necessary...

        // check for intersections on each line
        for (List<Vector3f> line : lines) {
            Vector3f inter = this.lineIntersection(line.get(0), line.get(1));
            if (inter == null) continue;
            intersections.add(inter);
        }

        return intersections;
    }

    private List<Vector3f> createLine(Vector3f a, Vector3f b) {
        List<Vector3f> points = new ArrayList<>();
        points.add(a);
        points.add(b);
        return points;
    }

    private Vector3f lineIntersection(Vector3f a, Vector3f b) {
        // https://gamedev.stackexchange.com/questions/111100/intersection-of-a-line-and-a-rectangle
        float A1 = this.end.y - this.startPos.y;
        float B1 = this.end.x - this.startPos.x;

        float A2 = a.y - b.y;
        float B2 = a.x - b.x;

        float delta = A1 * B2 - A2 * B1;
        if (delta < 1e-6 && delta > -1 * 1e-6) {
            return null;
        }

        float C2 = A2 * a.x + B2 * a.y;
        float C1 = A1 * this.startPos.x + B1 * this.startPos.y;

        float invdelta = 1/delta;
        return new Vector3f((B2 * C1 - B1 * C2) * invdelta, (A1 * C2 - A2 * C1) * invdelta, this.startPos.z);
    }
}
