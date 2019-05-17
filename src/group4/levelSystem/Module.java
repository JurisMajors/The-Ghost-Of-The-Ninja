package group4.levelSystem;

import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines the interface for modules that can be used to create levels
 * A module represents a height * width grid on which entities can be positioned
 *      entities may overlap multiple cells of the grid
 * A module can have entries and exits that can link to other modules in a levelSystem (links defined on levelSystem basis)
 */
public abstract class Module {

    // Define the size of the module grid
    // All modules should be of the same size to keep things simple
    public static final int height = 64;
    public static final int width = 64;

    // Keep track of the level that this module instance is part of
    private Level level;

    // List that keeps track of all the entities in the module
    private List<Entity> entities;

    // Keep track of the part of the module that is currently in screen-view
    // We will do this by keeping the bottom left corner in a variable and the visible region will be between
    // that point and the point (bottomleft.x + Main.SCREEN_WIDTH, bottomleft.y + Main.SCREEN_HEIGHT)
    private Vector3f screenPosition;

    // Projection matrix for the current module
    private Matrix4f pr_matrix;


    /**
     * Default construct, which constructs an empty module
     * If you want the module to be constructed with some default entities, please override @code{constructLevel()}
     */
    public Module(Level l) {
        this.setup(l);
    }


    /**
     * This method is used to set up the module in its initial state or reset the module to its initial state
     */
    private final void setup(Level l) {
        if (l == null) throw new IllegalArgumentException("Module: Level cannot be null");

        this.level = l;
        this.entities = new ArrayList<>();
        this.constructModule();
        this.screenPosition = this.getStartScreenWindow();
        this.renewProjectionMatrix();
    }


    /**
     * This method is used to reset the module to its initial state
     */
    public void reset() {
        this.setup(this.level);
    }


    /**
     * Populates @code{this.entities} with default entities for the module
     */
    protected abstract void constructModule();


    /**
     * Return the initial position of the screen window
     */
    protected abstract Vector3f getStartScreenWindow();


    /**
     * Return the initial position of the player in the module
     */
    public abstract Vector3f getPlayerInitialPosition();


    /**
     * Load the current module into the engine, so it gets rendered
     */
    public final void load() {
        for (Entity e : this.entities) {
            TheEngine.getInstance().addEntity(e);
        }
    }


    /**
     * Unload the current module from the engine, to stop it from being rendered
     */
    public final void unload() {
        for (Entity e : this.entities) {
            TheEngine.getInstance().removeEntity(e);
        }
    }


    /**
     * Sets screen window to new position, also updates the projection matrix
     * @param newPos the new bottom-left position of the screen window for the module
     */
    public void updateScreenWindow(Vector3f newPos) {
        this.screenPosition = newPos;
        this.renewProjectionMatrix();
    }


    /**
     * Update the projection matrix based on the current screen window
     */
    private void renewProjectionMatrix() {
        this.pr_matrix = Matrix4f.orthographic(this.screenPosition.x, this.screenPosition.x + Main.SCREEN_WIDTH,
                this.screenPosition.y, this.screenPosition.y + Main.SCREEN_HEIGHT, -1.0f, 1.0f);
    }


    /**
     * Get the current projection matrix based on the current screen window
     */
    public Matrix4f getProjectionMatrix() {
        return this.pr_matrix;
    }


    /**
     * Add an entity to the module
     * @param e the entity to add to the module
     */
    public void addEntity(Entity e) {
        this.entities.add(e);
    }


    /**
     * Remove an entity from the module
     * @param e the entity to remove from the module
     */
    public void removeEntity(Entity e) {
        this.entities.remove(e);
    }


    /**
     * Get an iterator of the entities that are in this module
     */
    public  Iterable<Entity> getEntities() {
        return this.entities;
    }
}
