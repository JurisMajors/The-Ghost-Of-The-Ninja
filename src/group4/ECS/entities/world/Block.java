package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;
import group4.utils.ShaderParser;

/**
 * Block entity for demonstration purposes
 */
public class Block extends Entity {

    /**
     * Construct a simple block (flexible texture and shader) in a certain position of certain size
     *
     * @param p The position of the block (lower left corner)
     * @param d The dimensions of the block
     * @param shader The shader for rendering the block
     * @param texture texture to use for the block
     */
    public Block(Vector3f p, Vector3f d, Shader shader, Texture texture) {

        // add needed components
        this.add(new PositionComponent(p));
        this.add(new DimensionComponent(d));

        // create basic graphics component covering the dimension of this block
        this.add(new GraphicsComponent(shader, texture, d));

        // register to engine
        TheEngine.getInstance().addEntity(this);

    }

}
