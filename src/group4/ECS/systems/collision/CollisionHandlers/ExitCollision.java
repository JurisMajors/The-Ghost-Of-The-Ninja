package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.world.Exit;

public class ExitCollision extends AbstractCollisionHandler<Exit> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new ExitCollision();

    @Override
    public void collision(Exit e, CollisionComponent cc) {
        if (!cc.collisions.isEmpty()) {
            // remove the exit collision, it will be handled from the player side
            cc.collisions.clear();
        }
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
