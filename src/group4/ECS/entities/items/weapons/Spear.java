package group4.ECS.entities.items.weapons;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.entities.items.Item;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Spear extends Item {

    public Spear() {

        // weapon stats
        int dmg = 15;
        float rateOfDamage = 0.05f;
        Vector3f hitBox = new Vector3f(1.6f, 0.4f, 0.0f);
        Vector3f hitboxOffset = new Vector3f(2.3f, 0.0f, 0.0f);

        // graphics stats

        // Construct vertex array
        float[] vertices = new float[] {
                0, 0, 0,
                0, 0.2f, 0,
                2.4f, 0.2f, 0,
                2.4f, 0, 0,
        };

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        // Construct texture coords
        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        Shader shader = Shader.SIMPLE;
        // TODO: texture for spear
        Texture texture = Texture.DEBUG;

        this.add(new MeleeWeaponComponent(dmg, rateOfDamage, hitBox, hitboxOffset));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));

        // TODO: position for rendering
        // this.add(new PositionComponent(pos));

    }

}
