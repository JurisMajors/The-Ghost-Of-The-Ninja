package group4.levelSystem;

import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.maths.Vector3f;
import group4.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileLevel extends Level {
    private List<String> modulePaths;
    private String ghostDir;

    public FileLevel(String levelRoot) {
        super(levelRoot);
    }

    /**
     * This function is used to load in the ghostPaths and modulePaths, essentially presenting all the files that should
     * be used in the specific level.
     */
    private void configurePaths() {
        this.modulePaths = FileUtils.getFilePaths(this.levelRoot + "/modules");
        this.ghostDir = this.levelRoot + "/ghosts/";
    }


    @Override
    protected Module createRoot() {
        /* Find the actual module files here. Would have liked to have done this in the constructor, but it is impossible
        to call a function before super(). */
        configurePaths();

        // Load in the 0th module. I think this is based on lexicographic ordering of the filenames.
        return new Module(this, this.modulePaths.get(0), this.ghostDir); // Change .get(#) to the module you wish to load by default. For testing purposes. Should be ZERO.
    }

    @Override
    protected void createAdditionalModules() {
        // Load in all other modules found.
        for (int i = 1; i < this.modulePaths.size(); i++) {
            this.addModule(new Module(this, this.modulePaths.get(i), this.ghostDir));
        }
    }

    @Override
    protected Player createPlayer() {
        // We don't care about the player position, as that will be initialized on level switching
        return new HierarchicalPlayer(new Vector3f(), this);
    }

    @Override
    protected void configExits() {
        for (Module m : this.modules) {
            for (Exit e : m.getExits()) {
                this.setExitAction(
                        e, // The exit that we want to set the action for
                        new ExitAction(this) { // Make sure to pass in the level in the constructor, so we can call back to it
                            @Override
                            public void exit() { // The actual action to execute
                                this.callBackLevel.switchModule(this.callBackLevel.getModuleReference(e.targetModule)); // Switch to the Simple Module once the exit is reached
                            }
                        }
                );
            }
        }
    }
}
