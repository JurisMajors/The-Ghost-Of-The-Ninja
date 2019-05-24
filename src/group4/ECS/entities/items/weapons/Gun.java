package group4.ECS.entities.items.weapons;

import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.items.Item;
import group4.maths.Vector3f;

public class Gun extends Item {

    // Construct vertex array
    protected float[] vertices = new float[] {
            0, 0, 0,
            0, 1.0f, 0,
            2.0f, 1.0f, 0,
            2.0f, 0, 0,
    };

    // Construct index array (used for geometry mesh)
    protected byte[] indices = new byte[] {
            0, 1, 2,
            2, 3, 0
    };

    // Construct texture coords
    protected float[] tcs = new float[] {
            0, 1,
            0, 0,
            1, 0,
            1, 1
    };

    /**
     * @param position position of Gun
     */
    public Gun(Vector3f position) {
        // add needed components
        this.add(new PositionComponent(position));
    }

}
