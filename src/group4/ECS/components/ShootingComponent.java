package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class ShootingComponent implements Component {
    public Vector3f position; //position where the bullets appear with respect to the entity position
    public int rate; //number of frames between two consecutive shots
    public int wait; //used to count frames, shoot when wait=rate

    /**
     * Creates a shooting component
     *
     * @param position position where the bullets appear with respect to the entity position
     */
    public ShootingComponent(Vector3f position) {
        this.position = position;
        this.rate = 1;
        this.wait = 1;
    }

    /**
     * Creates a shooting component
     *
     * @param position position where the bullets appear with respect to the entity position
     * @param rate     number of frames between two consecutive shots
     */
    public ShootingComponent(Vector3f position, int rate) {
        this.position = position;
        this.rate = rate;
        this.wait = rate;
    }
}
