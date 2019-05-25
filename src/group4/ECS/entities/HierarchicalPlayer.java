package group4.ECS.entities;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class HierarchicalPlayer extends Player implements GraphicsHierarchy {

    /**
     * dimension of player aka bounding box, ghost inherits in order to apply texture
     */
    protected Vector3f dimension = new Vector3f(1.0f, 2.0f, 0.0f);


    /**
     * hierarchy of graphics components
     */
    public List<BodyPart> hierarchy = new ArrayList<>();


    /**
     * Creates a player
     *
     * @param position center point of player
     * @param level the level that the player is part of
     */
    public HierarchicalPlayer(Vector3f position, Level level) {
        super(position, level);

        // Set the correct dimension component (will automatically remove the old one)
        this.add(new DimensionComponent(this.dimension));

        // Remove the graphics component, as the container won't have one
        this.remove(GraphicsComponent.class);

        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.NOTHINGNESS, dimension));

        // Construct the hierarchy of the player
        this.createHierarchy();
    }


    /**
     * Create the entities for the hierarchy
     */
    protected void createHierarchy() {
        // Add the torso
        Vector3f TorsoRelativePosition = new Vector3f(0.3f, 0.8f, 0.0f);
        Vector3f TorsoDimension = new Vector3f(0.4f, 0.8f, 0.0f);
        BodyPart torso = new BodyPart(this, TorsoRelativePosition, TorsoDimension, 0, Texture.DEBUG);
        this.hierarchy.add(torso);

        // Add the head (slightly above the torso)
        Vector3f HeadRelativePosition = new Vector3f(0.05f, 0.9f, 0.0f);
        Vector3f HeadDimension = new Vector3f(0.3f, 0.3f, 0.0f);
        BodyPart head = new BodyPart(torso, HeadRelativePosition, HeadDimension, 0, Texture.DEBUG);
        this.hierarchy.add(head);

        // Add right leg (take into account that the leg is rotated)
        Vector3f upperLegDimension =  new Vector3f(0.15f, 0.5f, 0.0f);
        Vector3f lowerLegDimension = new Vector3f(0.12f, 0.4f, 0.0f);

        BodyPart rLegUpper = new BodyPart(torso, new Vector3f(0.2f, 0.05f, 0.0f), upperLegDimension, 150, Texture.DEBUG);
        this.hierarchy.add(rLegUpper);
        BodyPart rLegLower = new BodyPart(rLegUpper, upperLegDimension, lowerLegDimension, 70, Texture.DEBUG);
        this.hierarchy.add(rLegLower);

        // Add left leg
        // TODO: should somehow be behind the right leg if moving to the right, and other way around if moving to left
        BodyPart lLegUpper = new BodyPart(torso, new Vector3f(0.2f, 0.05f, 0.0f), upperLegDimension, 190, Texture.DEBUG);
        this.hierarchy.add(lLegUpper);
        BodyPart lLegLower = new BodyPart(lLegUpper, upperLegDimension, lowerLegDimension, 70, Texture.DEBUG);
        this.hierarchy.add(lLegLower);

        // TODO: Position arms in front or behind player depending on movement direction
        // Add right arm
        Vector3f upperArmDimension = new Vector3f(0.1f, 0.5f, 0.0f);
        Vector3f lowerArmDimension = new Vector3f( 0.08f, 0.4f, 0.0f);

        BodyPart rArmUpper = new BodyPart(torso, new Vector3f(0.15f, 0.6f, 0.0f), upperArmDimension, 90, Texture.DEBUG);
        this.hierarchy.add(rArmUpper);
        BodyPart rArmLower = new BodyPart(rArmUpper, upperArmDimension, lowerArmDimension, -90, Texture.DEBUG);
        this.hierarchy.add(rArmLower);

        // Add left arm
        BodyPart lArmUpper = new BodyPart(torso, new Vector3f(0.15f, 0.6f, 0.0f), upperArmDimension, 120, Texture.DEBUG);
        this.hierarchy.add(lArmUpper);
        BodyPart lArmLower = new BodyPart(lArmUpper, upperArmDimension, lowerArmDimension, -60, Texture.DEBUG);
        this.hierarchy.add(lArmLower);
    }


    /**
     * Get the model matrix for the Hierarchical Player (just a translation matrix)
     */
    public Matrix4f getModelMatrix() {
        return Matrix4f.translate(this.getComponent(PositionComponent.class).position);
    }


}
