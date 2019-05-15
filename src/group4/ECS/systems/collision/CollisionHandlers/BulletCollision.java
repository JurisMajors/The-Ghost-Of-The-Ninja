package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.items.weapons.Bullet;

public class BulletCollision extends AbstractCollisionHandler<Bullet> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new BulletCollision();

    @Override
    public void collision(Bullet e, CollisionComponent cc) {

    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
