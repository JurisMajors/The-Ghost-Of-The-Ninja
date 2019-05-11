package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class Gun extends Entity {

    // Construct vertex array
    protected float[] vertices = new float[] {
            0, 0, 0,
            0, 1.0f, 0,
            2.0f, 1.0f, 0,
            2.0f, 0, 0,
    };

    // Construct index array (used for triangle mesh)
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

    public Gun(Vector3f p) {

        // add needed components
        // TODO adjust to new definition of position component
        // this.add(new PositionComponent(p));

        // register to engine
        TheEngine.getInstance().addEntity(this);

    }

}
