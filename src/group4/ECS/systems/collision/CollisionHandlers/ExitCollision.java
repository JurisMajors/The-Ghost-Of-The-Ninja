package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.world.Exit;

public class ExitCollision extends AbstractCollisionHandler<Exit> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new ExitCollision();

    @Override
    public void collision(Exit e, CollisionComponent cc) {
        System.out.println("Reached exit");
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }
}