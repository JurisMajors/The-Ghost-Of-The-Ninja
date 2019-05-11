package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

// TODO: This is temporary and can be removed when a better alternative is available

public class Player extends Entity {

    /**
     * Creates a player
     *
     * @param position      center point of player
     * @param dimension     along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention
     * @param velocity      velocity vector of player
     * @param velocityRange restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and velocity.y<=velocityRange.y
     * @param acceleration  acceleration vector of player
     * @param gravity       acceleration vector of player due to gravity
     * @param shader        shader for the player
     * @param texture       texture for the player
     */
    public Player(Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, Vector3f gravity, Shader shader, Texture texture) {
        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(velocity, velocityRange));
        this.add(new GravityComponent(gravity));
        this.add(new GraphicsComponent(shader, texture, dimension));
        // TODO: one of these two should go
        this.add(new PlayerInputComponent());
        this.add(new PlayerComponent());

        // register to engine
        TheEngine.getInstance().addEntity(this);
    }

    /**
     * Creates a player with 0 starting velocity, velocityRange of (1,1,0), 0 starting accelerating and default 9.81 gravity.
     *
     * @param position  center point of player
     * @param dimension along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention
     * @param shader    shader for the player
     * @param texture   texture for the player
     */
    public Player(Vector3f position, Vector3f dimension, Shader shader, Texture texture) {
        // use other constructor to create player
        this(position, dimension, new Vector3f(), new Vector3f(1, 1, 0), new Vector3f(), new Vector3f(0, 9.81f, 0), shader, texture);
    }

}
