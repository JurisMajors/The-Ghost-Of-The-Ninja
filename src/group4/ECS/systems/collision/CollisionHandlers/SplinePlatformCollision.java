package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.world.SplinePlatform;

public class SplinePlatformCollision extends AbstractCollisionHandler<SplinePlatform> {

    /**
     * Singleton
     **/
    private static AbstractCollisionHandler me = new PlatformCollision();

    @Override
    public void collision(SplinePlatform e, CollisionComponent cc) {

    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }
}
