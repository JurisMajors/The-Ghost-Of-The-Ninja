package group4.ECS.entities;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.game.IKEndEffector;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import group4.utils.DebugUtils;

import java.util.ArrayList;
import java.util.List;


public class HierarchicalPlayer extends Player implements GraphicsHierarchy {

    /**
     * dimension of player aka bounding box, ghost inherits in order to apply texture
     */
    protected Vector3f dimension = new Vector3f(1.0f, 2.0f, 0.0f);


    /**
     * Hierarchy of graphics components
     */
    public List<BodyPart> hierarchy = new ArrayList<>();
    public List<IKEndEffector> IKHandles = new ArrayList<>();

    /**
     * Directly access the bodyparts in the hierarchy
     */
    protected BodyPart torso;
    protected BodyPart rightLegUpper;
    protected BodyPart rightLegLower;
    protected BodyPart leftLegUpper;
    protected BodyPart leftLegLower;


    /**
     * Definition of body part dimensions
     */
    Vector3f upperLegDimension =  new Vector3f(0.15f, 0.5f, 0.0f);
    Vector3f lowerLegDimension = new Vector3f(0.12f, 0.4f, 0.0f);

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

        // Add a transparent GraphicsComponent to register this entity to the render system (will automatically remove the old one)
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.NOTHINGNESS, dimension, false));

        // Add an animation component to register this entity to the AnimationSystem
        this.add(new AnimationComponent());

        // Construct the hierarchy of the player
        this.createHierarchy();
    }


    /**
     * Create the entities for the hierarchy
     */
    protected void createHierarchy() {
        // Calculate and fix position of the hip
        Vector3f hipOffset = new Vector3f(this.dimension.x / 2, 0.8f, 0.0f);

        // Draw torso to visualise hip position
        Vector3f TorsoDimension = new Vector3f(0.4f, 0.8f, 0.0f);
        torso = new BodyPart(this, hipOffset, TorsoDimension, 0, Texture.DEBUG);
        this.hierarchy.add(torso);

        // Set the position of the foot for the right leg
        Vector3f rightFootOffset = new Vector3f(this.dimension.x / 3, 0.0f, 0.0f);

        // Draw the right leg
        float[] rightLegAngles = this.getLimbAngles(hipOffset, rightFootOffset, upperLegDimension.y, lowerLegDimension.y);
        rightLegUpper = new BodyPart(torso, new Vector3f(), upperLegDimension, rightLegAngles[0], Texture.DEBUG);
        rightLegLower = new BodyPart(rightLegUpper, new Vector3f(0.0f, upperLegDimension.y, 0.0f), lowerLegDimension, rightLegAngles[1], Texture.DEBUG);
        this.hierarchy.add(rightLegUpper);
        this.hierarchy.add(rightLegLower);
        this.IKHandles.add(new IKEndEffector(rightLegUpper, rightLegLower, hipOffset, rightFootOffset, "foot_R"));

        // Set the position of the foot for the left leg
        Vector3f leftFootOffset = new Vector3f(this.dimension.x, 0.5f, 0.0f);

        // Draw the left leg
        float[] leftLegAngles = this.getLimbAngles(hipOffset, leftFootOffset, upperLegDimension.y, lowerLegDimension.y);
        leftLegUpper = new BodyPart(torso, new Vector3f(), upperLegDimension, leftLegAngles[0], Texture.DEBUG);
        leftLegLower = new BodyPart(leftLegUpper, new Vector3f(0.0f, upperLegDimension.y, 0.0f), lowerLegDimension, leftLegAngles[1], Texture.DEBUG);
        this.hierarchy.add(leftLegUpper);
        this.hierarchy.add(leftLegLower);
        this.IKHandles.add(new IKEndEffector(leftLegUpper, leftLegLower, hipOffset, leftFootOffset, "foot_L"));

    }


    /**
     * Get the model matrix for the Hierarchical Player (just a translation matrix)
     */
    public Matrix4f getModelMatrix() {
        return Matrix4f.translate(this.getComponent(PositionComponent.class).position);
    }


    /**
     * Calculate the angles of two joints for e.g. a leg or arm based on two set positions and lengths of limb parts
     * @param bodySidePosition the position of the limb on the body side (e.g. the hip position)
     * @param limbEndPosition the position of the other end of the limb (e.g. the ankle)
     * @param upperLimbLength the length of the first part of the limb seen from the body (e.g. upper leg)
     * @param lowerLimbLength the length of the second part of the limb seen from the body (e.g. lower leg)
     * @return float[2] where [0] is the angle of the joint of the bodySidePosition (e.g. hip)
     *      and [1] is the angle of the second joint of the limb (e.g. knee)
     */
    // See {root}/images/limbAngles.jpg for a drawing of all angles, most are calculated using the cosine law
    public float[] getLimbAngles(Vector3f bodySidePosition, Vector3f limbEndPosition,
                                  float upperLimbLength, float lowerLimbLength) {
        // Array to store the calculated result angles
        float[] result = new float[2];

        // Calculate the offset between the bodySidePosition and the limbEndPosition
        Vector3f offset = limbEndPosition.sub(bodySidePosition);
        float offsetLength = offset.length();

        // Calculate Alpha2
        double alpha2 = Math.toDegrees(Math.atan(Math.abs(offset.x / offset.y)));

        // Calculate Alpha1
        double alpha1 = Math.toDegrees(Math.acos(
                (upperLimbLength * upperLimbLength + offsetLength * offsetLength - lowerLimbLength * lowerLimbLength)
                        / (2 * upperLimbLength * offsetLength)));

        // Calculate Alpha0, which is the angle for the bodySidePosition joint, so store it as well
        if (offset.x > 0) {
            if (offset.y < 0) {
                result[0] = (float) (180 - alpha1 - alpha2);
            } else {
                result[0] = (float) (alpha2 - alpha1);
            }
        } else {
            if (offset.y < 0) {
                result[0] = (float) (180 - alpha1 + alpha2);
            } else {
                result[0] = (float) (360 - alpha1 - alpha2);
            }
        }

        // Calculate Beta1
        double beta1 = Math.toDegrees(Math.acos(
                (upperLimbLength * upperLimbLength + lowerLimbLength * lowerLimbLength - offsetLength * offsetLength)
                        / (2 * upperLimbLength * lowerLimbLength)));

        // Calculate Beta0, which is the angle for the second joint, so store it as well
        result[1] = (float) (180 - beta1);

        // Return the angle vector
        return result;
    }


}
