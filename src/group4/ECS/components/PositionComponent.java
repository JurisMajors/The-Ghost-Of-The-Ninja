package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class PositionComponent implements Component {
    public Vector3f position;
    public Vector3f dimension;

    /**
     * Creates a position component
     *
     * @param position  left-bottom-back corner of the cuboid representing the entity
     * @param dimension such that the right-top-front corner of the cuboid representing the entity is position+dimension
     */
    public PositionComponent(Vector3f position, Vector3f dimension) {
        this.position = position;
        this.dimension = dimension;
    }
}
