package group4.simpleEntitySystem;

import group4.levelSystem.Module;
import group4.maths.Vector3f;

/**
 * This class can be used to define entities for the game
 */
public class Entity {

    // Module of the entity
    private Module module;

    // Position of the entity
    private Vector3f position;

    // Dimensions of the entity
    private Vector3f dimensions;

    // Possible graphics component
    protected GraphicsComponent gc = null;

    /**
     * Construct an entity in a certain position
     * @param p The position to create the entity in
     */
    public Entity(Module m, Vector3f p, Vector3f d) {
        this.module = m;
        this.position = p;
        this.dimensions = d;
    }


    /**
     * Get the module of an entity
     */
    public Module getModule() {
        return this.module;
    }


    /**
     * Get the position of an entity
     */
    public Vector3f getPosition() {
        return this.position;
    }


    /**
     * Get the dimensions of an entity
     */
    public Vector3f getDimensions() {
        return this.dimensions;
    }

    /**
     * Draw the entity if it has a graphics component
     */
    public void render() {
        // If the current entity is a graphical entity, draw it
        if (this.gc != null) {
            this.gc.render();
        }
    }

}
