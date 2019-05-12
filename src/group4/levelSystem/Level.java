package group4.levelSystem;

import java.util.ArrayList;
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


    /**
     * Initializes the level using factory & template method
     * Override @code{createRoot} and @code{createAdditionalModules} to specify the modules to be used for the level
     */
    public Level() {
        this.modules = new ArrayList<>();

        this.rootModule = this.createRoot();
        this.addModule(this.rootModule);

        this.createAdditionalModules();

        this.currentModule = this.rootModule;

        this.checkSanity();

        this.rootModule.load();
    }


    /**
     * Checks the sanity of the level
     * @Throws IllegalStateException if the level is not sane
     */
    private final void checkSanity() {
        if (this.rootModule == null)
            throw new IllegalStateException("Level: the root module cannot be null");

        if (!this.modules.contains(this.rootModule))
            throw new IllegalStateException("Level: the root modules should be included in the level-modules");

        if (this.currentModule == null)
            throw new IllegalStateException("Level: the current module cannot be null");
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
     * @param m The module to add to this level
     */
    protected final void addModule(Module m) {
        this.modules.add(m);
    }


    /**
     * Switches the level to the next module
     */
    protected final void switchModule(Module m) {
        if (!this.modules.contains(m)) throw new IllegalArgumentException("Level: you cannot switch to a module that is not part of the Level");

        // Unload the old module
        this.currentModule.unload();

        // Switch the current module to the next module
        this.currentModule = m;

        // Load the new module
        this.currentModule.load();
    }


    /**
     * Get the current module of the level
     */
    public Module getCurrentModule() {
        return this.currentModule;
    }
}
