package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.CollisionComponent;
import group4.ECS.components.DamageComponent;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.systems.collision.CollisionHandlers.BulletCollision;
import group4.maths.Vector3f;

public class Bullet extends Entity {

    // bounding box
    protected Vector3f dimension = new Vector3f(0.3f, 0.3f, 0.0f);

    // Construct vertex array
    protected float[] vertices = new float[] {
            0, 0, 0,
            0, dimension.y, 0,
            dimension.x, dimension.y, 0,
            dimension.x, 0, 0,
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

    /**
     * @param position of Bullet
     */
    public Bullet(Vector3f position, int dmg) {
        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CollisionComponent(BulletCollision.getInstance()));
        this.add(new DamageComponent(dmg));
    }

}
