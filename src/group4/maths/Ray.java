package group4.maths;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Mappers;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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
    Vector3f cast(List<Entity> entities) {
        Vector3f closestIntersection = null; // current closest intersection of the ray
        float curDist = Float.MAX_VALUE;
        // for each entity calculate the intersection
        for (Entity e : entities) {
            List<Vector3f> intersections = this.intersects(e);
            if (intersections == null) continue;

            for (Vector3f inter : intersections) {
                // calculate intersections distance
                float interDist = inter.euclidDist(this.startPos);
                // update the closest intersection and distance if necessary;
                if (interDist < curDist) {
                    curDist = interDist;
                    closestIntersection = inter;
                }
            }
        }
        // TODO: Couple it with the entity so that we can use the type of the entity as the input too
        return closestIntersection;
    }

    private Vector3f getDimension(PositionComponent pc) {
        // TODO: get dimensions from Venislavs branch
        return null;
    }

    /**
     *  Ray intersection point with a single entity
     * @param entity the entity to check for an intersection
     * @return null if no intersectiom, otherwise the positions of intersections with the  bounding box of the entity
     */
    List<Vector3f> intersects(Entity entity) {
        PositionComponent posComp = Mappers.positionMapper.get(entity);
        Vector3f dim = getDimension(posComp);
        Vector3f centr = posComp.position;
        return null;
    }
}
