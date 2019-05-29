package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.levelSystem.ExitAction;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLevel extends Level {
    private List<String> modulePaths;
    private List<String> ghostPaths;

    public FileLevel(String levelRoot) {
        super(levelRoot);
    }

    /**
     * This function is used to load in the ghostPaths and modulePaths, essentially presenting all the files that should
     * be used in the specific level.
     */
    private void configurePaths() {
        this.modulePaths = this.getFilePaths(this.levelRoot + "/modules");
        this.ghostPaths = this.getFilePaths(this.levelRoot + "/ghosts");

        // Some informative feedback to the console.
        if (this.modulePaths.size() == 0 || this.ghostPaths.size() == 0) {
            System.err.println("ERROR : No modules or no ghost files.");
        } else if (this.modulePaths.size() < this.ghostPaths.size()) {
            System.err.println("WARNING : MODULE count < GHOST count. Trimming # of ghosts.");
            this.ghostPaths = this.ghostPaths.subList(0, this.modulePaths.size());
        } else { // if (this.modulePaths.size() > this.ghostPaths.size()) {
            System.err.println("WARNING : MODULE count > GHOST count. Extending # of ghosts with the last ghost in the list.");
            while (this.modulePaths.size() > this.ghostPaths.size()) {
                this.ghostPaths.add(this.ghostPaths.get(this.ghostPaths.size() - 1));
            }
        }
    }

    private List<String> getFilePaths(String folder) {
        List<String> filePaths = new ArrayList<>();
        File folder_fd = new File(folder);
        for (final File fileEntry : folder_fd.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                filePaths.add(fileEntry.getPath());
            }
        }
        return filePaths;
    }

    @Override
    protected Module createRoot() {
        /* Find the actual module files here. Would have liked to have done this in the constructor, but it is impossible
        to call a function before super(). */
        configurePaths();

        // Load in the 0th module. I think this is based on lexicographic ordering of the filenames.
        return new Module(this, this.modulePaths.get(0), this.ghostPaths.get(0)); // Change .get(#) to the module you wish to load by default. For testing purposes. Should be ZERO.
    }

    @Override
    protected void createAdditionalModules() {
        // Load in all other modules found.
        for (int i = 1; i < this.modulePaths.size(); i++) {
            this.addModule(new Module(this, this.modulePaths.get(i), this.ghostPaths.get(i)));
        }
    }

    @Override
    protected Player createPlayer() {
        // We don't care about the player position, as that will be initialized on level switching
        return new Player(new Vector3f(), this);
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
