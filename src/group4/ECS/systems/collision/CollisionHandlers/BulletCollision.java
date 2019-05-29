package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.etc.TheEngine;

public class BulletCollision extends AbstractCollisionHandler<Bullet> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new BulletCollision();

    @Override
    public void collision(Bullet e, CollisionComponent cc) {
        if (!cc.collisions.isEmpty()) {
            // if has collided with something remove it from the engine
            // this still allows to process knockback
            // and damage for other entities, since its stored within the
            // set of collisions in the other entity
            TheEngine.getInstance().removeEntity(e);
            // clear the set so that we
            // dont do displacement of the bullet in the UncollidingSystem
            cc.collisions.clear();
        }
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
