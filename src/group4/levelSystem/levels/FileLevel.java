package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.ShootingJumpingWalkingMob;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.TheEngine;
import group4.levelSystem.ExitAction;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.TestModule;
import group4.maths.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLevel extends Level {
    private List<String> modulePaths;

    public FileLevel(String levelRoot) {
        super(levelRoot);
    }

    private void findModulePaths() {
        this.modulePaths = new ArrayList<>();

        File folder = new File(this.levelRoot);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
//                System.err.println(fileEntry.getPath());
                this.modulePaths.add(fileEntry.getPath());
            }
        }
    }

    @Override
    protected Module createRoot() {
        /* Find the actual module files here. Would have liked to have done this in the constructor, but it is impossible
        to call a function before super(). */
        findModulePaths();

        // Load in the 0th module. I think this is based on lexicographic ordering of the filenames.
        return new Module(this, this.modulePaths.get(1), "module-01-02-ghost"); // Change .get(#) to the module you wish to load by default. For testing purposes. Should be ZERO.
    }

    @Override
    protected void createAdditionalModules() {
        // Load in all other modules found.
        for (int i = 1; i < this.modulePaths.size(); i++) {
            this.addModule(new Module(this, this.modulePaths.get(i), "module-01-02-ghost"));
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
//                System.err.println("e: " + e.targetModule);
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
