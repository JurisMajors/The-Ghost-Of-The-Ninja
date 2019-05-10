package group4.levelSystem.modules;

import com.badlogic.gdx.math.Vector3;
import group4.ECS.entities.Player;
import group4.maths.Vector3f;
import group4.ECS.entities.Block;
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
            if (i % 3 == 0 && i % 4 != 0) {
                Block beautifulBlock = new Block(new Vector3f(i * 2.0f, 2.0f, 0.0f),
                        new Vector3f(2.0f, 2.0f, 0.0f), "src/group4/res/shaders/simple", "src/group4/res/textures/debug.jpeg");

                this.addEntity(beautifulBlock);
            }
        }
        Player player = new Player(new Vector3f(0.0f, 3.0f, 0.0f),
                new Vector3f(2.0f, 2.0f, 0.0f), "src/group4/res/shaders/simple", "src/group4/res/textures/debug.jpeg");
        this.addEntity(player);
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }
}
