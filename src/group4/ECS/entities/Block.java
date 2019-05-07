package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class Block extends Entity {

    /**
     * Construct an entity in a certain position (position should indicate bottom left of the block) of certain size
     *
     * @param p The position to create the entity in
     * @param d The dimensions of the block
     */
    public Block(Vector3f p, Vector3f d, String shader, String texture) {

        // Construct vertex array
        float[] vertices = new float[] {
                0, 0, d.z,
                0, d.y,d.z,
                d.x, d.y, d.z,
                d.x, 0, d.z,
        };

        // Construct index array
        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        // Construct texture coords
        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        this.add(new PositionComponent(p));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));
        TheEngine.getInstance().addEntity(this);

    }

}
