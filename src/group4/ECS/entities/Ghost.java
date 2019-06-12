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


        super.createHierarchy();
    }
}
