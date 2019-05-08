package group4.simpleEntitySystem.entities;

import group4.levelSystem.Module;
import group4.simpleEntitySystem.Entity;
import group4.simpleEntitySystem.GraphicsComponent;
import group4.maths.Vector3f;

public class Block extends Entity {

    /**
     * Construct an entity in a certain position (position should indicate bottom left of the block) of certain size
     *
     * @param p The position to create the entity in
     * @param d The dimensions of the block
     */
    public Block(Module m, Vector3f p, Vector3f d) {
        super(m, p, d);

        // Construct vertex array
        float[] vertices = new float[] {
                0, 0, 0,
                0, d.y,0,
                d.x, d.y, 0,
                d.x, 0, 0,
        };

        // Construct index array
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

        // Initialize the graphics component
        this.gc = new GraphicsComponent(this, "src/group4/res/shaders/simple", "src/group4/res/textures/debug.jpeg", vertices, indices, tcs, this.getPosition());
    }

}
