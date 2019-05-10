package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class RangeComponent implements Component {
    public Vector3f lbbCorner;
    public Vector3f rtfCorner;

    /**
     * Creates a range component
     *
     * @param lbbCorner left-bottom-back corner of the cuboid representing the accessible range of the map for the (center of the) entity
     * @param rtfCorner right-top-front corner of the cuboid representing the accessible range of the map for the (center of the) entity
     */
    public RangeComponent(Vector3f lbbCorner, Vector3f rtfCorner) {
        this.lbbCorner = lbbCorner;
        this.rtfCorner = rtfCorner;
    }
}
