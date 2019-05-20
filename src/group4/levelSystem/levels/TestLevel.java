package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.TestModule;
import group4.maths.Vector3f;

public class TestLevel extends Level {

    @Override
    protected Module createRoot() {
        return new TestModule(this, "test-59");
    }

    @Override
    protected void createAdditionalModules() {
        // For now I'll not add additional modules to this level
        return;
    }

    @Override
    protected Player createPlayer() {
        // We don't care about the player position, as that will be initialized on level switching
        return new Player(new Vector3f(), this);
    }

}
