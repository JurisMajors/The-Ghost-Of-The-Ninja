package group4.levelSystem.levels;

import group4.ECS.entities.Player;
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
        // Add the SimpleModule as well
        this.addModule(new SimpleModule(this));
    }

    @Override
    protected Player createPlayer() {
        // We don't care about the player position, as that will be initialized on level switching
        return new Player(new Vector3f(), this);
    }

    @Override
    protected void configExits() {

    }

}
