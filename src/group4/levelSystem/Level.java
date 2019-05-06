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
    protected Module _rootModule;

    // Keep a list of modules in this level
    protected List<Module> _modules;

    // Keep track of the module currently on screen
    private Module _currentModule;


    /**
     * Initializes the level using factory & template method
     * Override @code{_createRoot} and @code{_createAdditionalModules} to specify the modules to be used for the level
     */
    public Level() {
        this._modules = new ArrayList<>();

        this._rootModule = this._createRoot();
        this._modules.add(this._rootModule);

        this._createAdditionalModules();

        this._currentModule = this._rootModule;

        this._checkSanity();
    }


    /**
     * Checks the sanity of the level
     * @Throws IllegalStateException if the level is not sane
     */
    private void _checkSanity() {
        if (this._rootModule == null)
            throw new IllegalStateException("Level: the root module cannot be null");

        if (!this._modules.contains(this._rootModule))
            throw new IllegalStateException("Level: the root modules should be included in the level-modules");

        if (this._currentModule == null)
            throw new IllegalStateException("Level: the current module cannot be null");
    }


    /**
     * This method should return the module to be used as the root module for the level
     */
    protected abstract Module _createRoot();


    /**
     * This method should add additional modules to the level if desired
     */
    protected abstract void _createAdditionalModules();


    /**
     * Switches the level to the next module
     */
    protected void _switchModule(Module m) {
        if (!this._modules.contains(m)) throw new IllegalArgumentException("Level: you cannot switch to a module that is not part of the Level");

        // TODO: Remove the entities of the old module from the engine
        this._currentModule = m;
        // TODO: Add the entities of the new module to the engine
        // TODO: Reposition the player to the entry point of the new module
    }
}

// TODO: the drawing of the level should be done by the ECS engine?
// TODO: somehow link exits of modules to other modules
// TODO: when/how does the level end? Exit to special "end module"?
