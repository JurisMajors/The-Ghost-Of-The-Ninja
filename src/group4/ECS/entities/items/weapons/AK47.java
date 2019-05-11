package group4.ECS.entities.items.weapons;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.WeaponComponent;
import group4.ECS.etc.EntityConst;
import group4.maths.Vector3f;

public class AK47 extends Gun {

    public AK47(Vector3f p) {
        super(p);

        // AK47 texture (64 x 32) & shader (temporary simple shader)
        String shader = "src/group4/res/shaders/simple";
        String texture = "src/group4/res/textures/weapons/AK47.png";

        // add needed components
        // TODO graphic comp definition
        //this.add(new GraphicsComponent(shader, texture,
        //        this.vertices, this.indices, this.tcs));

        // TODO: damage-system
        this.add(new WeaponComponent(600, EntityConst.BulletType.MACHINEGUN));

    }

}
