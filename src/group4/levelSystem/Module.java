package group4.levelSystem;

import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines the interface for modules that can be used to create levels
 * A module represents a _height * _width grid on which entities can be positioned
 *      Entities may overlap multiple cells of the grid
 * A module can have entries and exits that can link to other modules in a levelSystem (links defined on levelSystem basis)
 */
public class Module {

    // Define the size of the module grid
    // All modules should be of the same size to keep things simple
    public static final int _height = 64;
    public static final int _width = 64;

    // Keep track of the level that this module instance is part of
    private Level _level;

    // List that keeps track of all the entities in the module
    private List<Entity> _entities;


    /**
     * Default construct, which constructs an empty module
     * If you want the module to be constructed with some default entities, please override @code{constructLevel()}
     */
    public Module(Level l) {
        if (l == null) throw new IllegalArgumentException("Module: Level cannot be null");

        this._level = l;
        this._entities = new ArrayList<>();
        this._constructModule();
    }


    /**
     * Populates @code{this.entities} with default entities for the module
     */
    protected void _constructModule() {}


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

    // TODO: somehow link exits (entities) to other modules so we can call this._level.switchModule(targetModule)
    // TODO: somehow define the entry point of the level for the player
}
