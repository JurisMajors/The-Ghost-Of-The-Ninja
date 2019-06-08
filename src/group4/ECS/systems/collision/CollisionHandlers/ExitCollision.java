package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.world.Exit;
import group4.ECS.systems.collision.CollisionData;

public class ExitCollision extends AbstractCollisionHandler<Exit> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new ExitCollision();

    @Override
    public void collision(Exit e, CollisionComponent cc) {
        if (!cc.collisions.isEmpty()) {
            // remove the exit collision, it will be handled from the player side
            for (CollisionData cd : cc.collisions) {
                if (cd.entity instanceof Ghost) { // kill ghost if he has reached the exit
                    cd.entity.getComponent(HealthComponent.class).health = 0;
                    ((Ghost) cd.entity).best = true;
                }
            }
            cc.collisions.clear();
        }
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
