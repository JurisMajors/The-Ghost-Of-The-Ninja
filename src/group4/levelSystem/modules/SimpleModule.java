package group4.levelSystem.modules;

import group4.ECS.entities.items.weapons.AK47;
import group4.ECS.entities.items.weapons.Bullet;
import group4.ECS.entities.items.weapons.Gun;
import group4.ECS.entities.items.weapons.MachineGunBullet;
import group4.ECS.entities.Camera;
import group4.ECS.entities.Player;
import group4.graphics.Shader;
import group4.graphics.Texture;
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
                        new Vector3f(2.0f, 2.0f, 0.0f), Shader.SIMPLE, Texture.DEBUG);
                this.addEntity(beautifulBlock);
            }
            if (i % 3 == 0 && i % 4 != 0) {
                Block beautifulBlock = new Block(new Vector3f(i * 2.0f, 2.0f, 0.0f),
                        new Vector3f(2.0f, 2.0f, 0.0f), Shader.SIMPLE, Texture.DEBUG);
                this.addEntity(beautifulBlock);
            }

            Gun gun = new AK47(new Vector3f( 2.0f, 2.0f, 0.0f));

            Bullet bullet_0 = new MachineGunBullet(new Vector3f(2.2f, 3.0f, 0.0f));
            Bullet bullet_1 = new MachineGunBullet(new Vector3f(2.2f, 3.2f, 0.0f));
            Bullet bullet_2 = new MachineGunBullet(new Vector3f(2.2f, 3.4f, 0.0f));
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
