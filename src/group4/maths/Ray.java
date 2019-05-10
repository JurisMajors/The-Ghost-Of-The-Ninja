package group4.maths;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Mappers;

import java.util.List;

public class Ray {
    /** starting position of the ray **/
    Vector3f startPos;
    /** direction of the ray **/
    Vector3f dir;

    public Ray(Vector3f startingPos, Vector3f dir) {
        this.startPos = startingPos;
        this.dir = dir;
    }

    /**
     * Cast the ray to the given entities
     * @param entities the entities that the ray might intersect
     * @return position at first intersection of a bounding box
     */
    Vector3f cast(List<Entity> entities) {
        return null;
    }

    private Vector3f getDimension(PositionComponent pc) {
        // TODO: get dimensions from Venislavas branch
        return null;
    }

    /**
     *  Ray intersection point with a single entity
     * @param entity the entity to check for an intersection
     * @return null if no intersectiom, otherwise the position
     */
    Vector3f intersects(Entity entity) {
        PositionComponent posComp = Mappers.positionMapper.get(entity);
        Vector3f dim = getDimension(posComp);
        Vector3f centr = posComp.position;
        return null;
    }
}
