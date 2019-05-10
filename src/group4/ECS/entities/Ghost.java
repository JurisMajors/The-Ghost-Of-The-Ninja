package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.AI.Brain;
import group4.ECS.components.AIComponent;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.maths.Vector3f;

/**
 * The Ghost helper class (does not get registered on construction!)
 */
public class Ghost extends Entity {

    public Ghost(Brain brain, Vector3f p, Vector3f d) {

        // Construct vertex array
        float[] vertices = new float[] {
                0, 0, 0,
                0, d.y, 0,
                d.x, d.y, 0,
                d.x, 0, 0,
        };

        // Construct index array (used for triangle mesh)
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

        // TODO: Ghost texture & shader
        String shader = null;
        String texture = null;

        // add necessary components
        this.add(new AIComponent(brain));
        this.add(new PositionComponent(p));
        this.add(new DimensionComponent(d));
        this.add(new GraphicsComponent(shader, texture,
                vertices, indices, tcs));

    }

}
