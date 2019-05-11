package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class Bullet extends Entity {

    // bounding box
    protected Vector3f d = new Vector3f(0.3f, 0.3f, 0.0f);

    // Construct vertex array
    protected float[] vertices = new float[] {
            0, 0, 0,
            0, d.y, 0,
            d.x, d.y, 0,
            d.x, 0, 0,
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

    public Bullet(Vector3f p) {

        // add needed components
        // TODO pos comp definition
        //this.add(new PositionComponent(p));
        this.add(new DimensionComponent(d));

        // register to engine
        TheEngine.getInstance().addEntity(this);

    }

}
