package group4.levelSystem.modules;

import com.badlogic.gdx.math.Vector3;
import group4.ECS.entities.Camera;
import group4.ECS.entities.Player;
import group4.game.Main;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Matrix4f;
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
                        new Vector3f(2.0f, 2.0f, 0.0f), Shader.SIMPLE, Texture.DEBUG);

                this.addEntity(beautifulBlock);
            }
            if (i % 3 == 0 && i % 4 != 0) {
                Block beautifulBlock = new Block(new Vector3f(i * 2.0f, 2.0f, 0.0f),
                        new Vector3f(2.0f, 2.0f, 0.0f),  Shader.SIMPLE, Texture.DEBUG);

                this.addEntity(beautifulBlock);
            }
        }

        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Player player = new Player(new Vector3f(0.0f, 3.0f, 0.0f),
                new Vector3f(2.0f, 2.0f, 0.0f),  Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(player); // Adding the player to the module (which adds it to the engine?)

        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Camera camera = new Camera();
        this.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }
}
