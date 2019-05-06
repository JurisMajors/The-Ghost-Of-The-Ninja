package group4.simpleEntitySystem.entities;

import group4.simpleEntitySystem.Entity;
import group4.simpleEntitySystem.GraphicsComponent;
import group4.maths.Vector3f;
import group4.maths.Vector3i;

public class Block extends Entity {

    // Hold the dimensions of this block
    private Vector3f _dimensions;

    // Hold the colour of this block
    private Vector3i _colour;

    /**
     * Construct an entity in a certain position (position should indicate top left of the block) of certain size
     *
     * @param p The position to create the entity in
     * @param d The dimensions of the block
     * @param c The colour of this block
     */
    public Block(Vector3f p, Vector3f d, Vector3i c) {
        super(p);
        this._dimensions = d;
        this._colour = c;
        this._gc = new GraphicsComponent();
    }

}
