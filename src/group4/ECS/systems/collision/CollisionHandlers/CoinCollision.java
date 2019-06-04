package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.identities.CoinComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.ScoreComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.items.consumables.Coin;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;

public class CoinCollision extends AbstractCollisionHandler<Coin> {

    /**
     * Singleton
     **/
    private static AbstractCollisionHandler me = new CoinCollision();

    @Override
    public void collision(Coin e, CollisionComponent cc) {
        for (CollisionData cd : cc.collisions) {
            // only handle collision with coin and player
            if (cd.entity instanceof Player && !(cd.entity instanceof Ghost)) {
                ScoreComponent playerScore = Mappers.scoreMapper.get(cd.entity);
                CoinComponent coinComponent = Mappers.coinMapper.get(e);

                // update score
                playerScore.addScore(coinComponent.value);

                // remove coin from the module
                ((Player) cd.entity).level.getCurrentModule().removeEntity(e);
            }
        }
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
