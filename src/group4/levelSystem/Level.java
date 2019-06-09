package group4.levelSystem;

import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.TheEngine;
import group4.game.GameState;
import group4.game.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class defines the interface for a Level of the game
 * A level consists of modules, of which one is visible at each moment
 * Entries and exits of modules are linked on a Level basis
 */
public abstract class Level {

    // This is the module where the level starts
    protected Module rootModule;

    // Keep a list of modules in this level
    protected List<Module> modules;

    // Keep track of the module currently on screen
    private Module currentModule;

    // Keep track of the level-wide player entity
    private Player player;

    // Keep track of ExitActions that are registered per Exit
    private HashMap<Exit, ExitAction> exitActions;

    // Keep track of the root folder for a level, Optional!
    protected String levelRoot;

    /**
     * Initializes the level using factory & template method
     * Override @code{createRoot} and @code{createAdditionalModules} to specify the modules to be used for the level
     */
    public Level() {
        this.modules = new ArrayList<>();                   // Initialize the modules list

        this.rootModule = this.createRoot();                // Create and add the root module
        this.addModule(this.rootModule);

        this.createAdditionalModules();                     // Create and add the additional modules

        this.player = this.createPlayer();                  // Create the level wide player instance
        TheEngine.getInstance().addEntity(this.player);     // Register the level wide player instance to the engine

        this.switchModule(this.rootModule);                 // Switch the current module to the root module
        // Also takes care of positioning the player entity etc.

        this.exitActions = new HashMap<>();                 // Initialize the exit action list

        this.configExits();                                 // Configure the exits

        this.checkSanity();                                 // Check that the level was created appropriately
        Main.setState(GameState.PLAYING);                   // Ensure the system knows the game is playing.
    }

    /**
     * Initializes the level using factory & template method. Bases modules on a given folder path.
     * Override @code{createRoot} and @code{createAdditionalModules} to specify the modules to be used for the level
     *
     * @param levelRoot String, the root path of the level
     */
    public Level(String levelRoot) {
        this.levelRoot = levelRoot;

        // I want to set the levelRoot before calling this(), which java won't allow. Hence this is a copy paste
        // of the other constructor.
        this.modules = new ArrayList<>();                   // Initialize the modules list

        this.rootModule = this.createRoot();                // Create and add the root module
        this.addModule(this.rootModule);

        this.createAdditionalModules();                     // Create and add the additional modules

        this.player = this.createPlayer();                  // Create the level wide player instance
        TheEngine.getInstance().addEntity(this.player);     // Register the level wide player instance to the engine

        this.switchModule(this.rootModule);                 // Switch the current module to the root module
        // Also takes care of positioning the player entity etc.

        this.exitActions = new HashMap<>();                 // Initialize the exit action list

        this.configExits();                                 // Configure the exits

        this.checkSanity();                                 // Check that the level was created appropriately
        Main.setState(GameState.PLAYING);
    }

    /**
     * Checks the sanity of the level
     *
     * @Throws IllegalStateException if the level is not sane
     */
    private final void checkSanity() {
        if (this.rootModule == null)
            throw new IllegalStateException("Level: the root module cannot be null");

        if (!this.modules.contains(this.rootModule))
            throw new IllegalStateException("Level: the root modules should be included in the level-modules");

        if (this.currentModule == null)
            throw new IllegalStateException("Level: the current module cannot be null");

        if (this.player == null)
            throw new IllegalStateException("Level: the player entity cannot be null");

        for (Module m : this.modules) {
            for (Exit e : m.getExits()) {
                if (!this.exitActions.containsKey(e))
                    throw new IllegalStateException("Level: not all exits in the level where configured");
            }
        }
    }


    /**
     * This method should return the module to be used as the root module for the level
     */
    protected abstract Module createRoot();


    /**
     * This method should add additional modules to the level if desired
     */
    protected abstract void createAdditionalModules();


    /**
     * This method adds a module to this level
     *
     * @param m The module to add to this level
     */
    protected final void addModule(Module m) {
        this.modules.add(m);
    }


    /**
     * This method should return the player object to be used in the level
     */
    protected abstract Player createPlayer();


    /**
     * This method should set an exit action for each exit of each module
     */
    protected abstract void configExits();


    /**
     * Switches the level to the next module
     */
    public final void switchModule(Module m) {
        if (!this.modules.contains(m))
            throw new IllegalArgumentException("Level: you cannot switch to a module that is not part of the Level");

        // Unload the old module (if there is an old module)
        if (this.currentModule != null) {
            this.currentModule.unload();
        }

        // Switch the current module to the next module
        this.currentModule = m;

        // Update the player's position to the initial position in the new module
        this.player.getComponent(PositionComponent.class).position = this.currentModule.getPlayerInitialPosition();

        // Load the new module
        this.currentModule.load();
    }


    /**
     * Get the current module of the level
     */
    public Module getCurrentModule() {
        return this.currentModule;
    }


    /**
     * Get the player of the level
     */
    public Player getPlayer() {
        return this.player;
    }


    /**
     * Set the exit action for a certain exit
     *
     * @param e The exit
     * @param a The exit action
     */
    public void setExitAction(Exit e, ExitAction a) {
        this.exitActions.put(e, a);
    }


    /**
     * Get the reference to the i^th module of the level
     */
    public Module getModuleReference(int i) {
        if (i >= this.modules.size())
            throw new IllegalArgumentException("Level: the module you requested does not exist");

        return this.modules.get(i);
    }


    /**
     * Let the level react to an exit being reached
     *
     * @param e The exit that was reached
     */
    public void handleExit(Exit e) {
        if (!this.exitActions.containsKey(e))
            throw new IllegalArgumentException("Level: no exit action is defined for this exit");

        this.exitActions.get(e).exit();
    }


    /**
     * Reset the level to initial state
     */
    public void reset() {
        // Unload current module
        this.currentModule.unload();

        // Reset all modules
        for (Module m : this.modules) {
            m.reset();
        }

        // Set the root module as the current module
        this.currentModule = this.rootModule;

        // Configure the exits correctly
        this.configExits();

        // Create a new player and set it to correct position
        TheEngine.getInstance().removeEntity(this.player);
        this.player = this.createPlayer();
        TheEngine.getInstance().addEntity(this.player);
        this.player.getComponent(PositionComponent.class).position = this.currentModule.getPlayerInitialPosition();

        // Load the module
        this.currentModule.load();
    }
}
