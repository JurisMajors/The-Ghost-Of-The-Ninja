package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.SimpleModule;
import group4.maths.Vector3f;

public class SimpleLevel extends Level {

    @Override
    protected Module createRoot() {
        return new SimpleModule(this, null);
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

    @Override
    protected void configExits() {
        // This level's modules don't have exits
        return;
    }

}
