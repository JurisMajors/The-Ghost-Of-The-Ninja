package group4.levelSystem.modules;

import group4.ECS.entities.items.weapons.AK47;
import group4.ECS.entities.items.weapons.Gun;
import group4.maths.Vector3f;
import group4.ECS.entities.world.Block;
import group4.levelSystem.Level;
import group4.levelSystem.Module;

public class SimpleModule extends Module {

    public SimpleModule(Level l) {
        super(l);
    }

    @Override
    protected void constructModule() {
        for (int i = 0; i < 32; i++) {
            if (i % 4 != 0) {
                Block beautifulBlock = new Block(new Vector3f(i * 2.0f, 0.0f, 0.0f),
                        new Vector3f(2.0f, 2.0f, 0.0f), "src/group4/res/shaders/simple", "src/group4/res/textures/debug.jpeg");

                this.addEntity(beautifulBlock);
            }

            Gun gun = new AK47(new Vector3f( 2.0f, 2.0f, 0.0f),
                    "src/group4/res/shaders/simple");
            this.addEntity(gun);
        }
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }
}
