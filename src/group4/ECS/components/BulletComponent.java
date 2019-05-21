package group4.ECS.components;

import com.badlogic.ashley.core.Component;

/**
 * BulletComponent, needed if the object needs to be registered as an Entity for the bullet movement system.
 */
public class BulletComponent implements Component {
    /**
     * Default constructor which creates the component. No data is currently associated with being a bullet.
     */
    public BulletComponent() {
    }

}
