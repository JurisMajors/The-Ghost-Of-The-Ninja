package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.items.consumables.HealthPotion;

public class PotionCollision extends AbstractCollisionHandler<HealthPotion> {

    /**
     * Singleton
     **/
    private static AbstractCollisionHandler me = new PotionCollision();

    @Override
    public void collision(HealthPotion e, CollisionComponent cc) {
        cc.collisions.clear();
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
