package group4.ECS.entities.mobs;

import group4.ECS.components.identities.FlyingMobComponent;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class FlyingMob extends Mob {

    /**
     * Creates a flying mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public FlyingMob(Vector3f position, Level l) {
        super(position, l);
        this.add(new FlyingMobComponent());
    }
}
