package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class GravityComponent implements Component {

    public Vector3f gravity;

    /**
     * default constructor for normal gravity
     */
    public GravityComponent() {
        gravity = new Vector3f(0.0f, 0.0098f, 0.0f);
    }

    /**
     * Creates a gravity component
     *
     * @param gravity acceleration vector due to gravity
     */
    public GravityComponent(Vector3f gravity) {
        this.gravity = gravity;
    }

}
