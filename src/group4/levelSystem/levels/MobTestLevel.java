package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.*;
import group4.ECS.etc.TheEngine;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.TestModule;
import group4.maths.Vector3f;

public class MobTestLevel extends Level {

    @Override
    protected Module createRoot() {
        return new TestModule(this);
    }

    @Override
    protected void createAdditionalModules() {
        // For now I'll not add additional modules to this level
        return;
    }

    @Override
    protected Player createPlayer() {
        //temporary add mob to the level here
        TheEngine.getInstance().addEntity(new ShootingJumpingWalkingMob(new Vector3f(15.0f, 10.0f, 0.0f)));

        // We don't care about the player position, as that will be initialized on level switching
        return new Player(new Vector3f());
    }

}
