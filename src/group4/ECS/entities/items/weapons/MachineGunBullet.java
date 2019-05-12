package group4.ECS.entities.items.weapons;

import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class MachineGunBullet extends Bullet {

    /**
     * @param position of Bullet
     */
    public MachineGunBullet(Vector3f position) {
        super(position);

        // bullet texture (16 x 16) & shader (temp simple shader)
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.MG_BULLET;

        // TODO: proper specs
        this.add(new PhysicsComponent(0.5f, 0.5f));
        this.add(new DamageComponent(10));
        this.add(new MovementComponent(new Vector3f(), new Vector3f()));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));
    }

}
