package group4.levelSystem.levels;

import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.SimpleModule;

public class SimpleLevel extends Level {

    @Override
    protected Module createRoot() {
        return new SimpleModule(this);
    }

    @Override
    protected void createAdditionalModules() {
        return;
    }

}
