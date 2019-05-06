package group4.levelSystem.modules;

import group4.simpleEntitySystem.entities.Block;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;
import group4.maths.Vector3i;

public class SimpleModule extends Module {

    public SimpleModule(Level l) {
        super(l);
    }

    @Override
    protected void _constructModule() {
        Block beautifulBlock = new Block(new Vector3f(3.0f, 5.0f, 0.0f),
                                            new Vector3f(5.0f, 5.0f, 0.0f));

        this._addEntity(beautifulBlock);
    }

}
