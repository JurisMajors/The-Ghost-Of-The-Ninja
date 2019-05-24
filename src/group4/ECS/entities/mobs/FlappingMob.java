package group4.ECS.entities.mobs;

import group4.ECS.components.identities.FlappingMobComponent;
import group4.ECS.systems.movement.MovementHandlers.FlappingMobMovementHandler;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class FlappingMob extends Mob {

    /**
     * Creates a flapping mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public FlappingMob(Vector3f position, Level l) {
        super(position, l, FlappingMobMovementHandler.getInstance());
        this.add(new FlappingMobComponent());
    }

    public static String getName() {
        return "FlappingMob";
    }
}
