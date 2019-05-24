package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
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
        // Add the torso
        BodyPart torso = new BodyPart(new Vector3f(0.2f, 0.5f, 0.0f), new Vector3f(0.6f, 1.0f, 0.0f), 0, Texture.DEBUG);
        this.hierarchy.add(torso);
    }


}
