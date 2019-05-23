package group4.ECS.systems.collision;

import com.badlogic.ashley.core.Entity;
import group4.maths.Vector3f;

public class CollisionData {

    // the entity that is being collided with
    public Entity entity;

    /**
     * the vector that needs to be added to the position entity that owns this collision data to make sure it
     * does not intersect with entity
     */
    public Vector3f displacement;

    /**
     * Vector used for spline collision, determines where to put the entity colliding with a spline.
     */
    public Vector3f newPos = null;

    /**
     * Creates new CollisionData.
     *
     * @param entity       entity which collides with the owner of this collision data.
     * @param displacement vector that needs to be added to the owners position to prevent the collision.
     */
    public CollisionData(Entity entity, Vector3f displacement) {
        this.entity = entity;
        this.displacement = displacement;
    }

    /**
     * Creates new CollisionData especially for splines.
     *
     * @param entity       entity which collides with the owner of this collision data.
     * @param displacement vector that needs to be added to the owners position to prevent the collision.
     * @param newPos       new position for the entity that should be displaced.
     */
    public CollisionData(Entity entity, Vector3f displacement, Vector3f newPos) {
        this.entity = entity;
        this.displacement = displacement;
        this.newPos = newPos;
    }


    @Override
    public boolean equals(Object o) {
        // only allow one collision per entity
        if (o instanceof CollisionData) {
            CollisionData cd = (CollisionData) o;
            return entity.equals(cd.entity);
        }

        return super.equals(o);
    }
}
