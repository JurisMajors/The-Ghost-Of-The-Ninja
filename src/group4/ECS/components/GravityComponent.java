package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class GravityComponent implements Component {
    public Vector3f gravity;

    /**
     * Creates a gravity component
     *
     * @param gravity acceleration vector due to gravity
     */
    public GravityComponent(Vector3f gravity) {
        this.gravity = gravity;
    }
}
