package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.AI.Brain;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

import java.io.IOException;

/**
 * The helper Ghost
 */
public class Ghost extends Player {

    /**
     * @param position center point of Ghost
     * @param level the level that the Ghost is part of
     * @param brain NN for training purposes
     */
    public Ghost (Vector3f position, Level level, Brain brain) {
        super(position, level);

        Shader shader = Shader.SIMPLE;
        // TODO: proper texture
        Texture texture = Texture.BRICK;

        // remove player graphics
        this.remove(GravityComponent.class);

        // add needed components
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new GhostComponent(brain));
    }

    public Ghost (Vector3f position, Level level, String brainPath) {
        this(position, level, new Brain(brainPath));
    }
}
