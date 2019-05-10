package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class RangeComponent implements Component {
    public Vector3f lbbCorner;
    public Vector3f rtfCorner;

    public RangeComponent(Vector3f lbbCorner, Vector3f rtfCorner) {
        this.lbbCorner = lbbCorner;
        this.rtfCorner = rtfCorner;
    }
}
