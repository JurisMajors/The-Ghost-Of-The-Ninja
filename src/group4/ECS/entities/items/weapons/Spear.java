package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Spear extends Entity {

    public Spear() {

        // weapon stats
        int dmg = 15;
        float cooldown = 0.65f;
        Vector3f hitBox = new Vector3f(1.4f, 0.4f, 0.0f);
        Vector3f hitboxOffset = new Vector3f(1.0f, 0.4f, 0.0f);

        // graphics stats

        // Construct vertex array
        float[] vertices = new float[]{
                0, 0, 0,
                0, 0.2f, 0,
                2.4f, 0.2f, 0,
                2.4f, 0, 0,
        };

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        // Construct texture coords
        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        Shader shader = Shader.SIMPLE;
        // TODO: texture for spear
        Texture texture = Texture.DEBUG;

        this.add(new MeleeWeaponComponent(dmg, cooldown, hitBox, hitboxOffset));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));

        // TODO: position for rendering
        // this.add(new PositionComponent(pos));

    }

}
