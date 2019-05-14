package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PlatformComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

import java.util.ArrayList;

public class Platform extends Entity {

    public Platform(Vector3f position, Shader shader, Texture texture) {

    }
    /**
     * Creates a static platform
     *
     * @param position  left-bottom-back corner of the cuboid representing the platform
     * @param dimension such that the right-top-front corner of the cuboid representing the platform is position+dimension
     * @param shader    shader for this platform
     * @param texture   texture for this platform
     */
    public Platform(Vector3f position, Vector3f dimension, Shader shader, Texture texture) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new PlatformComponent());

        // create the graphics component with a vertex array repeating the texture over this platform
        GraphicsComponent graphicsComponent = new GraphicsComponent(shader, texture, dimension);
//        GraphicsComponent graphicsComponent = new GraphicsComponent(shader, texture, dimension);
        this.add(graphicsComponent);
    }
}
