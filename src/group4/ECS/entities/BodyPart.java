package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class BodyPart extends Entity {

    /**
     * Relative position of the BodyPart w.r.t. to the bottom left position of the player
     */
    public Vector3f relativePosition;

    /**
     * Rotation of the BodyPart
     */
    protected float rotation;

    /**
     * Texture of the BodyPart
     */
    protected Texture texture;

    /**
     * Constructs a new body part
     * @param relativePosition position of body part relative to bottom left position of the player
     * @param dimension dimension of this body part
     * @param rotation the rotation of the body part
     * @param texture the texture to apply to the body part
     */
    public BodyPart(Vector3f relativePosition, Vector3f dimension, float rotation, Texture texture) {
        // Set relative position
        this.relativePosition = relativePosition;
        // Set the dimension
        this.add(new DimensionComponent(dimension));
        // Set the rotation
        this.rotation = rotation;
        // Set the texture
        this.texture = texture;
        // Create a graphics component
        this.add(new GraphicsComponent(Shader.SIMPLE, this.texture, dimension));
    }

}
