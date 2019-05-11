package group4.levelSystem;

import group4.ECS.etc.TheEngine;
import group4.levelSystem.modules.SimpleModule;

public class ModuleFactory {

    public static Module createModule(Level level) {
        TheEngine.getInstance().removeAllEntities();
        return new SimpleModule(level);
    }

}
