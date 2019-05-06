package group4.simpleEntitySystem;

import group4.levelSystem.Module;
import group4.maths.Vector3f;

/**
 * This class can be used to define entities for the game
 */
public class Entity {

    // Module of the entity
    private Module _module;

    // Position of the entity
    private Vector3f _position;

    // Dimensions of the entity
    private Vector3f _dimensions;

    // Possible graphics component
    protected GraphicsComponent _gc = null;

    /**
     * Construct an entity in a certain position
     * @param p The position to create the entity in
     */
    public Entity(Module m, Vector3f p, Vector3f d) {
        this._module = m;
        this._position = p;
        this._dimensions = d;
    }


    /**
     * Get the module of an entity
     */
    public Module _getModule() {
        return this._module;
    }


    /**
     * Get the position of an entity
     */
    public Vector3f _getPosition() {
        return this._position;
    }


    /**
     * Get the dimensions of an entity
     */
    public Vector3f _getDimensions() {
        return this._dimensions;
    }

    /**
     * Draw the entity if it has a graphics component
     */
    public void _render() {
        // If the current entity is a graphical entity, draw it
        if (this._gc != null) {
            this._gc._render();
        }
    }

}
