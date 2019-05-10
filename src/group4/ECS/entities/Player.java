package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PlayerInputComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class Player extends Entity {
    public Player(Vector3f p, Vector3f d, String shader, String texture) {
        float[] vertices = new float[] {
                0, 0, d.z,
                0, d.y,d.z,
                d.x, d.y, d.z,
                d.x, 0, d.z,
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

        // add needed components
        this.add(new PositionComponent(p));
        this.add(new MovementComponent(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f)));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));
        this.add(new PlayerInputComponent());

        // register to engine
        TheEngine.getInstance().addEntity(this);
    }
}