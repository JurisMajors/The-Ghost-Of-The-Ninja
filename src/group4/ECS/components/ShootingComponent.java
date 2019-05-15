package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class ShootingComponent implements Component {
    public Vector3f position;
    public int wait;
    public int rate;

    public ShootingComponent(Vector3f position) {
        this.position = position;
        this.rate=300;
        this.wait=300;
    }

    public ShootingComponent(Vector3f position, int rate) {
        this.position = position;
        this.rate=rate;
        this.wait=rate;
    }
}
