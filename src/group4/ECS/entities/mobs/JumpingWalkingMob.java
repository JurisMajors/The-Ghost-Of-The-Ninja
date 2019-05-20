package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class JumpingWalkingMob extends Mob {

    /**
     * Creates a jumping&walking mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public JumpingWalkingMob(Vector3f position, Level l) {
        super(position, l);
        this.add(new JumpingWalkingMobComponent());
    }
}
