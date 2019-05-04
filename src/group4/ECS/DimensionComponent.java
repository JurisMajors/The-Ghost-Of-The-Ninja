package group4.ECS;

import com.badlogic.ashley.core.Component;

public class DimensionComponent implements Component {

    // maybe a geometryComponent is better suited to be able to create non-rectangular
    // objects

    public float width = 0.0f;
    public float height = 0.0f;

}
