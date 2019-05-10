package group4.ECS.entities.items.weapons;

import group4.ECS.components.*;
import group4.maths.Vector3f;

public class MachineGunBullet extends Bullet {

    // TODO: possibly decouple shader from graphics ?? Passing on shader to each component
    // might be expensinve?
    public MachineGunBullet(Vector3f p) {
        super(p);

        // bullet texture (16 x 16) & shader (temp simple shadek)
        String shader = "src/group4/res/shaders/simple";
        String texture = "src/group4/res/textures/weapons/Bullet.png";

        // TODO: proper specs
        this.add(new PhysicsComponent(0.5f, 0.5f));
        this.add(new DamageComponent(10));
        this.add(new MovementComponent(new Vector3f(), new Vector3f()));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));

    }

}
