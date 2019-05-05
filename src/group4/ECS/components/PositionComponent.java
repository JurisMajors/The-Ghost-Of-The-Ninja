package group4.ECS.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    // TODO: left, right(, down, up) as int in enum?
    public int orientation = 0;

}
