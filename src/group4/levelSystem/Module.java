package group4.levelSystem;

import group4.AI.Brain;
import group4.AI.Evolver;
import group4.ECS.entities.Camera;
import group4.ECS.entities.Ghost;
import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import com.badlogic.ashley.core.Entity;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines the interface for modules that can be used to create levels
 * A module represents a height * width grid on which entities can be positioned
 *      entities may overlap multiple cells of the grid
 * A module can have entries and exits that can link to other modules in a levelSystem (links defined on levelSystem basis)
 */
public class Module {

    // Define the size of the module grid
    // All modules should be of the same size to keep things simple
    private int height;
    private int width;

    // Keep track of the level that this module instance is part of
    private Level level;

    // List that keeps track of all the entities in the module
    private List<Entity> entities;

    // ghost model
    private String ghostPath = null;

    // Keeps track of the initial player position
    private Vector3f initialPlayerPos;

    // JSON Object containing the tiled data
    JSONObject tiledData;


    /**
     * Default construct, which constructs a module based on a Tiled .tmx file
     */
    public Module(Level l, String TiledLevelLocation) {
        this(l, TiledLevelLocation,null);
    }

    public Module(Level l, String TiledLevelLocation, String ghostModelName) {
        if (ghostModelName != null) {
            this.ghostPath = Evolver.path + ghostModelName;
        }
        this.setup(l);
    }


    /**
     * This method is used to create a JSON object containing the tiled module information
     */
    private void loadTiledObject(String fileLocation) {
        // Try to read the tiled json file
        try {
            FileReader fileReader = new FileReader(fileLocation);

            // Construct the JSON object containing the tiled module information
            this.tiledData = new JSONObject(new JSONTokener(fileReader));

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Module: could not find the tiled module JSON file");
        }
    }



    /**
     * This method is used to set up the module in its initial state or reset the module to its initial state
     */
    private final void setup(Level l) {
        if (l == null) throw new IllegalArgumentException("Module: Level cannot be null");
        this.level = l;
        this.entities = new ArrayList<>();
        this.constructModule();
        this.addGhost();
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
    protected void constructModule() {
        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Camera camera = new Camera();
        this.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)
    }


    /**
     * Return the initial position of the player in the module
     */
    public Vector3f getPlayerInitialPosition() {
        return new Vector3f();
    }


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


    /**
     * Add a ghost to the current module
     */
    private void addGhost() throws IllegalStateException {
        if (Main.AI) return;

        if (this.entities == null) {
            throw new IllegalStateException("Adding ghost before initialized entities container");
        }
        if (this.ghostPath != null) {
            this.entities.add(new Ghost(this.getPlayerInitialPosition(), this.level,
                    new Brain(this.ghostPath)));
        } else {
            System.err.println("WARNING: Not loading ghost in module");
        }
    }


    /**
     * Get the width of the module grid
     */
    public int getWidth() {
        return this.width;
    }


    /**
     * Get the height of the module grid
     */
    public int getHeight() {
        return this.height;
    }
}
