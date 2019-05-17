package group4.levelSystem.levels;

import group4.levelSystem.Module;
import group4.levelSystem.modules.NoRenderTestModule;

public class NonRenderTestLevel extends TestLevel {
    @Override
    protected Module createRoot() {
        return new NoRenderTestModule(this);
    }
}
