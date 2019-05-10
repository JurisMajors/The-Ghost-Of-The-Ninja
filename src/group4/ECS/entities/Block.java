package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
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
     * Construct a simple block in a certain position of certain size
     *
     * @param p The position of the block (lower left corner)
     * @param d The dimensions of the block
     */
    public Block(Vector3f p, Vector3f d, String shaderPath, String texturePath) {

        // Construct vertex array
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
        this.add(new PositionComponent(p, d));

        Shader shader = ShaderParser.loadShader(shaderPath);
        Texture texture = new Texture(texturePath);

        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));

        // register to engine
        TheEngine.getInstance().addEntity(this);

    }

}
