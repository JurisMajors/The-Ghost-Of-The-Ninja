package group4.ECS.entities.mobs;

import group4.ECS.components.identities.JumpingMobComponent;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class JumpingMob extends Mob {

    /**
     * Creates a jumping mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public JumpingMob(Vector3f position, Level l) {
        super(position, l);
        this.add(new JumpingMobComponent());
    }
}
