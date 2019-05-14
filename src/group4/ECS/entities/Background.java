package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.graphics.RenderLayer.Layer;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

/**
 * Block entity for demonstration purposes
 */
public class Background extends Entity {
    /**
     * Construct a simple block (flexible texture and shader) in a certain position of certain size
     *
     * @param position The position of the block (lower left corner)
     * @param dimension The dimensions of the block
     * @param shader The shader for rendering the block
     * @param texture texture to use for the block
     */
    public Background(Vector3f position, Vector3f dimension, Shader shader, Texture texture) {
        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));

        // create basic graphics component covering the dimension of this block
        this.add(new GraphicsComponent(shader, texture, dimension, Layer.BACKGROUND));
    }

}
