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
    Module rootModule;

    // Keep a list of modules in this level
    List<Module> modules;


    /**
     * Call the create method of the level to initialize it
     */
    public Level() {
        this.modules = new ArrayList<>();
        this.create();
        this.checkSanity();
    }


    /**
     * Checks the sanity of the level
     * @Throws IllegalStateException if the level is not sane
     */
    private void checkSanity() {
        if (rootModule == null) throw new IllegalStateException("Level: the root module cannot be null");
        if (!modules.contains(rootModule)) throw new IllegalStateException("Level: the root modules should be included in the level-modules");
    }


    /**
     * This method should take care of adding all the modules to the level
     */
    protected abstract void create();
}
