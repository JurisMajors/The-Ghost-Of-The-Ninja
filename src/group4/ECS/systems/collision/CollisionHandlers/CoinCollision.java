package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.items.consumables.Coin;

public class CoinCollision extends AbstractCollisionHandler<Coin> {

    /**
     * Singleton
     **/
    private static AbstractCollisionHandler me = new CoinCollision();

    @Override
    public void collision(Coin e, CollisionComponent cc) {
        cc.collisions.clear();
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
