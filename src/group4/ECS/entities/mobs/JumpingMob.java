package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class JumpingMob extends Mob {

    /**
     * Creates a jumping mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public JumpingMob(Vector3f position) {
        super(position);
        this.add(new JumpingMobComponent());
    }
}
