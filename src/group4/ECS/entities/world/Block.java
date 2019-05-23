package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.systems.collision.CollisionHandlers.BlockCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

/**
 * Block entity for demonstration purposes
 */
public class Block extends Entity {
    // TODO: This and other tiles can be part of a more general entity, which we haven't thought out yet. Hence I didn't bother extending the comments. -Nico
    public Block(Vector3f position, Shader shader, Texture texture) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent());

        this.add(new GraphicsComponent(shader, texture, DimensionComponent.defaultTileDimension));
        this.add(new CollisionComponent(BlockCollision.getInstance()));

    }

    public Block(Vector3f position, Shader shader, Texture texture, float[] tileMapCoords) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent());
        this.add(new GraphicsComponent(shader, texture, DimensionComponent.defaultTileDimension, tileMapCoords));
        this.add(new CollisionComponent(BlockCollision.getInstance()));
    }


    /**
     * Construct a simple block (flexible texture and shader) in a certain position of certain size
     *
     * @param position The position of the block (lower left corner)
     * @param dimension The dimensions of the block
     * @param shader The shader for rendering the block
     * @param texture texture to use for the block
     */
    public Block(Vector3f position, Vector3f dimension, Shader shader, Texture texture) {
        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));

        // create basic graphics component covering the dimension of this block
        this.add(new CollisionComponent(BlockCollision.getInstance()));
        this.add(new GraphicsComponent(shader, texture, dimension));
    }

    public static String getName() {
        return "Exit";
    }
}
