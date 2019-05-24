package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.ShootingJumpingWalkingMob;
import group4.ECS.etc.TheEngine;
import group4.levelSystem.ExitAction;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.SimpleModule;
import group4.levelSystem.modules.TestModule;
import group4.maths.Vector3f;

public class TestLevel extends Level {

    @Override
    protected Module createRoot() {
        return new TestModule(this, "best-40");
    }

    @Override
    protected void createAdditionalModules() {
        // Add the SimpleModule as well (Tiled version)
        this.addModule(new Module(this, "./src/group4/levelSystem/modules/TiledModules/simpleModule.json", null));
    }

    @Override
    protected Player createPlayer() {
        //temporary add mob to the level here
        TheEngine.getInstance().addEntity(new ShootingJumpingWalkingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));
        // We don't care about the player position, as that will be initialized on level switching
        return new Player(new Vector3f(), this);
    }

    @Override
    protected void configExits() {
        // Configure the only exit of the level: the exit of the test module
        this.setExitAction(
                this.modules.get(0).getExits().get(0), // The exit that we want to set the action for
                new ExitAction(this) { // Make sure to pass in the level in the constructor, so we can call back to it
                    @Override
                    public void exit() { // The actual action to execute
                        this.callBackLevel.switchModule(this.callBackLevel.getModuleReference(1)); // Switch to the Simple Module once the exit is reached
                    }
                }
        );
    }

}
