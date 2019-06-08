package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

public class BodyPart extends Entity implements GraphicsHierarchy {

    /**
     * The entity that we will be used for the relative position of this body part
     */
    public GraphicsHierarchy parent;

    /**
     * Relative position of the BodyPart w.r.t. to the bottom left position of the parent
     */
    public Vector3f relativePosition;

    /**
     * Rotation of the BodyPart
     */
    public float rotation;

    /**
     * Texture of the BodyPart
     */
    protected Texture texture;

    /**
     * Constructs a new body part
     * @param parent the entity that this body part should be positioned to relatively
     * @param relativePosition position of body part relative to bottom left position of the parent
     * @param dimension dimension of this body part
     * @param rotation the rotation of the body part
     * @param texture the texture to apply to the body part
     */
    public BodyPart(GraphicsHierarchy parent, Vector3f relativePosition, Vector3f dimension, float rotation, Texture texture) {
        // Set the parent entity
        this.parent = parent;
        // Set relative position
        this.relativePosition = relativePosition;
        // Set the dimension
        this.add(new DimensionComponent(dimension));
        // Set the rotation
        this.rotation = rotation;
        // Set the texture
        this.texture = texture;
        // Create a graphics component
        this.add(new GraphicsComponent(Shader.SIMPLE, this.texture, dimension, true));
    }


    /**
     * Will return the model matrix for this body part
     */
    public Matrix4f getModelMatrix() {
        // Translate to parent position
        Matrix4f result = this.parent.getModelMatrix();

        // Translate relative to the parent position
        result = result.multiply(Matrix4f.translate(this.relativePosition));

        // Rotate the body part
        result = result.multiply(Matrix4f.rotate2D(this.rotation));

        // Return the model matrix
        return result;
    }

}
