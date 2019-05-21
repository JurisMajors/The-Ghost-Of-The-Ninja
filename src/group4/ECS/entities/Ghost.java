package group4.ECS.entities;

import group4.AI.Brain;
import group4.ECS.components.*;
import group4.ECS.components.identities.GhostComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;


/**
 * The helper Ghost
 */
public class Ghost extends Player {

    /**
     * @param position center point of Ghost
     * @param level    the level that the Ghost is part of
     * @param brain    NN for training purposes
     */
    public Ghost(Vector3f position, Level level, Brain brain) {
        super(position, level);

        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.BRICK;

        //// remove player graphics
        this.remove(GraphicsComponent.class);

        //// add needed components
        this.add(new GraphicsComponent(shader, texture, new Vector3f(1.0f, 1.0f, 0.0f)));
        this.add(new GhostComponent(brain));
    }

    public Ghost (Vector3f position, Level level, String brainPath) {
        this(position, level, new Brain(brainPath));
    }
}
