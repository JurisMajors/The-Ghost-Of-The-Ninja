package group4.levelSystem;

import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.maths.Vector3f;
import group4.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
        this.modulePaths = FileUtils.getFilePaths(this.levelRoot + "/modules");
        this.ghostPaths = new ArrayList<>();

        List<String> allGhostFiles = FileUtils.getFilePaths(this.levelRoot + "/ghosts");
        for (String modulePath : this.modulePaths) {
            boolean matched = false; // See if we find a ghost for this module
            String[] splits = modulePath.split("\\" + File.separator); // a\b\c.json => [a, b, c.json]
            String filePath = splits[splits.length - 1];                    // c.json
            String fileName = filePath.split("\\.")[0];               // c

            // Try to match vs all ghostPaths found. N^2, but N is guaranteed to be small. I.e. we won't have 10.000 modules or ghosts.
            for (String ghostPath : allGhostFiles) {
                splits = ghostPath.split("\\" + File.separator);
                filePath = splits[splits.length - 1];
                String ghostFileName = filePath.split("\\.")[0];
                if (ghostFileName.equals(fileName)) {
                    matched = true;
                    this.ghostPaths.add(ghostPath);
                }
            }

            // If we have not found a ghost for this module, add a null.
            if (!matched) {
                this.ghostPaths.add(null);
            }
        }
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
