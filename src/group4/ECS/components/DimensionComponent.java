package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class DimensionComponent implements Component {
    public Vector3f dimension;

    /**
     * Creates a position component
     *
     * @param dimension the width,height,depth of this component
     */
    public DimensionComponent(Vector3f dimension) {
        this.dimension = dimension;
    }
}
