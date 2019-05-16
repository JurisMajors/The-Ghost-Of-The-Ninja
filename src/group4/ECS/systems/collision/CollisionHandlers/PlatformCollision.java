package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.world.Platform;

public class PlatformCollision extends AbstractCollisionHandler<Platform> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new PlatformCollision();

    @Override
    public void collision(Platform e, CollisionComponent cc) {

    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }
}
