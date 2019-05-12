package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.AI.Brain;
import group4.ECS.PlayerRules;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Ghost extends Entity {

    public Ghost (Brain brain, Vector3f position, Vector3f dimension, Vector3f velocity, Vector3f velocityRange, Vector3f acceleration, Vector3f gravity, Shader shader, Texture texture) {
        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(velocity, velocityRange, acceleration));
        this.add(new GravityComponent(gravity));
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new GhostComponent(brain));

        // register to engine
        TheEngine.getInstance().addEntity(this);
    }

    public Ghost (Brain brain, Vector3f pos, Shader shader, Texture texture) {
        this(brain, pos, PlayerRules.dimension, PlayerRules.startVelocity, PlayerRules.velocityRange, PlayerRules.acceleration, PlayerRules.acceleration, shader, texture);
    }

}
