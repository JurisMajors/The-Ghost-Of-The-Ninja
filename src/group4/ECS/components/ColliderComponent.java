package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

/**
 * ColliderComponent, needed if the object needs to be registered as an Entity for the collision system.
 */
public class ColliderComponent implements Component {
    /**
     * Default constructor which creates the component. No data is currently associated with being a collider, though
     * this could hold parms like friction, bounciness, mass, ...
     */
    public ColliderComponent() {}

}
