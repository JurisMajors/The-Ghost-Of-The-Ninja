package group4.simpleEntitySystem;

import group4.maths.Vector3f;

/**
 * This class can be used to define entities for the game
 */
public class Entity {

    // Position of th entity
    private Vector3f _position;

    // Possible graphics component
    protected GraphicsComponent _gc = null;

    /**
     * Construct an entity in a certain position
     * @param p The position to create the entity in
     */
    public Entity(Vector3f p) {
        this._position = p;
    }


    /**
     * Get the position of an entity
     */
    public Vector3f _getPosition() {
        return this._position;
    }


    /**
     * Draw the entity if it has a graphics component
     */
    public void _render() {
        if (this._gc != null) {
            this._gc._render();
        }
    }

}
