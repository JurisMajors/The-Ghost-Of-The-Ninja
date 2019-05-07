package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class PositionComponent implements Component {

    public Vector3f position;
    // TODO: left, right(, down, up) as int in enum?
    public int orientation = 0;

    public PositionComponent(Vector3f position) {
        this.position = position;
    }

    public PositionComponent(Vector3f position, int orientation) {
        this.position = position;
        this.orientation = orientation;
    }

}
