package group4.ECS.entities;

import group4.ECS.components.GraphicsComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class HierarchicalPlayer extends Player {

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
        // TODO: Maybe we can make the positions of certain body parts depend on each other, e.g. the position of the head on the torso

        // Add the torso
        BodyPart torso = new BodyPart(new Vector3f(0.3f, 0.8f, 0.0f), new Vector3f(0.4f, 0.8f, 0.0f), 0, Texture.DEBUG);
        this.hierarchy.add(torso);

        // Add the head (slightly above the torso)
        BodyPart head = new BodyPart(new Vector3f(0.3f, 1.7f, 0.0f), new Vector3f(0.3f, 0.3f, 0.0f), 0, Texture.DEBUG);
        this.hierarchy.add(head);

        // Add upper right leg (take into account that the leg is rotated)
        BodyPart rLeg = new BodyPart(new Vector3f(0.4f, 0.8f, 0.0f), new Vector3f(0.2f, 0.4f, 0.0f), 150, Texture.DEBUG);

        // Add left leg
        // TODO: should somehow be behind the right leg
    }


}
