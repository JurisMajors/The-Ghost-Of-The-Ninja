package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class PositionComponent implements Component {
    public Vector3f position;
    public Vector3f dimension;

    public PositionComponent(Vector3f position, Vector3f dimension) {
        this.position = position;
        this.dimension = dimension;
    }
}
