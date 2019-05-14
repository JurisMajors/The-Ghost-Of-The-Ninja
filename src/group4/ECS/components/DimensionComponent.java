package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

/**
 * DimensionComponent aka Bounding Box, will be needed for Damage, Collision, AI
 */
public class DimensionComponent implements Component {
    public static Vector3f defaultTileDimension = new Vector3f(1.0f, 1.0f, 0.0f);
    public Vector3f dimension;

    /**
     * Creates a dimension component with the defaultTileDimension.
     */
    public DimensionComponent() {
        this.dimension = defaultTileDimension;
    }

    /**
     * Creates a dimension component based on the given dimension vector.
     *
     * @param dimension Vector3f, the (width, height, depth) of this component
     */
    public DimensionComponent(Vector3f dimension) {
        this.dimension = dimension;
    }

}
