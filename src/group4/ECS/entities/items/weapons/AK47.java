package group4.ECS.entities.items.weapons;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.etc.EntityConst;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class AK47 extends Gun {

    public AK47(Vector3f p) {
        super(p);

        // AK47 texture (64 x 32) & shader (temporary simple shader)
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.AK47;

        // add needed components
        // TODO graphic comp definition
        this.add(new GraphicsComponent(shader, texture,
                this.vertices, this.indices, this.tcs));

        // add weapon specs
        this.add(new RangeWeaponComponent(10, 15,
                new Vector3f(0.5f, 2.0f,0.0f), EntityConst.BulletType.MACHINEGUN));
    }

    public static String getName() {
        return "AK47";
    }
}
