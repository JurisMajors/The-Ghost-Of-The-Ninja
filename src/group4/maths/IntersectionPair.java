package group4.maths;

import com.badlogic.ashley.core.Entity;

/**
 * Container class used for {@link Ray}.
 * Contains a point at which a ray intersection with e Bounding Box happens happens
 * and the entity whose bounding box it is
 */
public class IntersectionPair {
    public Vector3f point;
    public Entity entity;

    IntersectionPair(Vector3f p, Entity e) {
        this.point = p;
        this.entity = e;
    }
}
