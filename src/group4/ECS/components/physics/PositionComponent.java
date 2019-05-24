package group4.ECS.components.physics;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class PositionComponent implements Component {

    public static int LEFT = 0;
    public static int RIGHT = 1;

    public Vector3f position;
    public Vector3f orientation;

    /**
     * default constructor
     *
     * @param position    left-bottom-back corner of the cuboid representing the entity
     */
    public PositionComponent(Vector3f position) {
        // right by default
        this.orientation = new Vector3f(1.0f, 0.0f, 0.0f);
        this.position = position;
    }

    /**
     * Creates a position component
     *
     * @param position    left-bottom-back corner of the cuboid representing the entity
     * @param orientation either left or right (0 or 1 respectively)
     */
    public PositionComponent(Vector3f position, int orientation) {
        this.position = position;

        if (orientation == LEFT) {
            this.orientation = new Vector3f(-1.0f, 0.0f, 0.0f);
        } else {
            this.orientation = new Vector3f(1.0f, 0.0f, 0.0f);
        }
    }

}
