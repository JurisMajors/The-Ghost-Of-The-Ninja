package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.mobs.Mob;

public class MobCollision extends AbstractCollisionHandler<Mob> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new MobCollision();

    @Override
    public void collision(Mob e, CollisionComponent cc) {

    }

    public static AbstractCollisionHandler getInstance(){
        return me;
    }
}
