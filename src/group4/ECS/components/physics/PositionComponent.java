package group4.ECS.components.physics;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class PositionComponent implements Component {
    public Vector3f position;

    /**
     * Creates a position component
     *
     * @param position  left-bottom-back corner of the cuboid representing the entity
     */
    public PositionComponent(Vector3f position) {
        this.position = position;
    }
}
