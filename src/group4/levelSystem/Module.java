package group4.levelSystem;

import group4.game.Main;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines the interface for modules that can be used to create levels
 * A module represents a _height * _width grid on which entities can be positioned
 *      entities may overlap multiple cells of the grid
 * A module can have entries and exits that can link to other modules in a levelSystem (links defined on levelSystem basis)
 */
public abstract class Module {

    // Define the size of the module grid
    // All modules should be of the same size to keep things simple
    public static final int _height = 64;
    public static final int _width = 64;

    // Keep track of the level that this module instance is part of
    private Level _level;

    // List that keeps track of all the entities in the module
    private List<Entity> _entities;

    // Keep track of the part of the module that is currently in screen-view
    // We will do this by keeping the bottom left corner in a variable and the visible region will be between
    // that point and the point (bottomleft.x + Main.SCREEN_WIDTH, bottomleft.y + Main.SCREEN_HEIGHT)
    private Vector3f _screenPosition;

    // Projection matrix for the current module
    private Matrix4f _pr_matrix;


    /**
     * Default construct, which constructs an empty module
     * If you want the module to be constructed with some default entities, please override @code{constructLevel()}
     */
    public Module(Level l) {
        if (l == null) throw new IllegalArgumentException("Module: Level cannot be null");

        this._level = l;
        this._entities = new ArrayList<>();
        this._constructModule();
        this._screenPosition = this._getStartScreenWindow();
        this._renewProjectionMatrix();
    }


    /**
     * Populates @code{this.entities} with default entities for the module
     */
    protected abstract void _constructModule();


    /**
     * Return the initial position of the screen window
     */
    protected abstract Vector3f _getStartScreenWindow();


    /**
     * Sets screen window to new position, also updates the projection matrix
     * @param newPos the new bottom-left position of the screen window for the module
     */
    public void _updateScreenWindow(Vector3f newPos) {
        this._screenPosition = newPos;
        this._renewProjectionMatrix();
    }


    /**
     * Update the projection matrix based on the current screen window
     */
    private void _renewProjectionMatrix() {
        this._pr_matrix = Matrix4f.orthographic(this._screenPosition.x, this._screenPosition.x + Main.SCREEN_WIDTH,
                this._screenPosition.y, this._screenPosition.y + Main.SCREEN_HEIGHT, -1.0f, 1.0f);
    }


    /**
     * Get the current projection matrix based on the current screen window
     */
    public Matrix4f _getProjectionMatrix() {
        return this._pr_matrix;
    }


    /**
     * Add an entity to the module
     * @param e the entity to add to the module
     */
    public void _addEntity(Entity e) {
        this._entities.add(e);
    }


    /**
     * Remove an entity from the module
     * @param e the entity to remove from the module
     */
    public void _removeEntity(Entity e) {
        this._entities.remove(e);
    }


    /**
     * Get an iterator of the entities that are in this module
     */
    public Iterator<Entity> _getEntities() {
        return this._entities.iterator();
    }


    /**
     * Draw the current state of the module
     */
//    public void _render() {
//        for (Entity e : this._entities) {
//            // Check entity is currently on (at least partly) screen, and if so, render it
//            if (    e._getPosition().x + e._getDimensions().x >= this._screenPosition.x &&
//                    e._getPosition().x <= this._screenPosition.x + Main.SCREEN_WIDTH &&
//                    e._getPosition().y + e._getDimensions().y >= this._screenPosition.y &&
//                    e._getPosition().y <= this._screenPosition.y + Main.SCREEN_HEIGHT) {
//                e._render();
//            }
//        }
//    }
}
