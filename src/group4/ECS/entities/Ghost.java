package group4.ECS.entities;

import group4.AI.Brain;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;


/**
 * The helper Ghost
 */
public class Ghost extends HierarchicalPlayer {
    public boolean best; // whether has reached the exit (used during training)

    public Player master = null; // the player which spawned the ghost
    public int endTotem; // totem identification to determine when it has reached end of its path

    public boolean helping = false; // whether this ghost is a helper
    public boolean carrying = false; // whether this ghost is a carrier

    protected Texture LegBlobTexture;

    /**
     * @param position center point of Ghost
     * @param level    the level that the Ghost is part of
     * @param brain    move maker of the ghost
     */
    public Ghost(Vector3f position, Level level, Brain brain) {
        super(position, level);
        best = false;
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.BRICK;

        //// remove player graphics
        this.remove(GraphicsComponent.class);

        //// add needed components
        this.add(new GraphicsComponent(shader, texture, this.dimension, false));
        this.add(new GhostComponent(brain));
    }

    public Ghost (Vector3f position, Level level, String brainPath) {
        this(position, level, new Brain(brainPath));
    }

    public Ghost (Level level, Brain brain, Player master) {
        this(master.getComponent(PositionComponent.class).position,
                level, brain);
        this.master = master;
    }
    public Ghost (Vector3f pos, Level level, Brain brain, Player master) {
        this(pos, level, brain);
        this.master = master;
    }

    public static String getName() {
        return "Ghost";
    }


    @Override
    protected void createHierarchy() {
        // Check which ghost we are, i.e. challenging or not, and set the appropriate textures
        if (this.level.getPlayer().challanging) { // Challenging ghost texture (red)
            TorsoTexture = Texture.RED_GHOST_TORSO;
            HeadTexture = Texture.RED_GHOST_HEAD;
            RightLowerLegTexture = Texture.RED_GHOST_LIGHT_LEG_LOWER;
            RightUpperLegTexture = Texture.RED_GHOST_LIGHT_LEG_UPPER;
            LeftLowerLegTexture = Texture.RED_GHOST_DARK_LEG_LOWER;
            LeftUpperLegTexture = Texture.RED_GHOST_DARK_LEG_UPPER;
            RightLowerArmTexture = Texture.RED_GHOST_LIGHT_ARM_LOWER;
            RightUpperArmTexture = Texture.RED_GHOST_LIGHT_ARM_UPPER;
            LeftLowerArmTexture = Texture.RED_GHOST_DARK_ARM_LOWER;
            LeftUpperArmTexture = Texture.RED_GHOST_DARK_ARM_UPPER;
            LegBlobTexture = Texture.RED_GHOST_LEG_BLOB;
        } else { // Normal ghost texture (blue)
            TorsoTexture = Texture.BLUE_GHOST_TORSO;
            HeadTexture = Texture.BLUE_GHOST_HEAD;
            RightLowerLegTexture = Texture.BLUE_GHOST_LIGHT_LEG_LOWER;
            RightUpperLegTexture = Texture.BLUE_GHOST_LIGHT_LEG_UPPER;
            LeftLowerLegTexture = Texture.BLUE_GHOST_DARK_LEG_LOWER;
            LeftUpperLegTexture = Texture.BLUE_GHOST_DARK_LEG_UPPER;
            RightLowerArmTexture = Texture.BLUE_GHOST_LIGHT_ARM_LOWER;
            RightUpperArmTexture = Texture.BLUE_GHOST_LIGHT_ARM_UPPER;
            LeftLowerArmTexture = Texture.BLUE_GHOST_DARK_ARM_LOWER;
            LeftUpperArmTexture = Texture.BLUE_GHOST_DARK_ARM_UPPER;
            LegBlobTexture = Texture.BLUE_GHOST_LEG_BLOB;
        }

        super.createHierarchy();

        BodyPart legBlob = new BodyPart(this.torso, new Vector3f(-0.09375f, -0.46875f,0.0f), new Vector3f(0.75f, 0.53125f, 0.0f), 0, LegBlobTexture);
        this.hierarchy.add(legBlob);
    }
}
