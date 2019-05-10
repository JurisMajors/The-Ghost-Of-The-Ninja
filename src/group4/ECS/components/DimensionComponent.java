package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

/**
 * DimensionComponent aka Bounding Box, will be needed for Damage, Collision, AI
 */
public class DimensionComponent implements Component {

    public Vector3f dimension;

    public DimensionComponent(Vector3f dimension) {
        this.dimension = dimension;
    }

}
