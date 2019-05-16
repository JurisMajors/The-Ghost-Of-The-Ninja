package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.items.weapons.Bullet;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.Mappers;

import java.util.Collection;
import java.util.Set;


/**
 * An example of how we might want to handle collisions per Entity
 */
public class PlayerCollision extends AbstractCollisionHandler<Player> {

    private static AbstractCollisionHandler me = new PlayerCollision();

    @Override
    public void collision(Player player, CollisionComponent cc) {
        Set<Entity> others = cc.collisions;
        // loop through all collisions and handle them accordingly
        for (Entity other : others) {
            // example
            if (other instanceof Mob) {
                handleMob(player, (Mob) other);
            } else if (other instanceof Bullet) {
                handleBullet(player, (Bullet) other);
            }
        }
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

    private static void handleMob(Player player, Mob mob) {
        // this is a placeholder to show how the system would work
    }

    private static void handleBullet(Player player, Bullet bullet) {
        // after player bullet interaction we dont want to fix their positions (because the bullet might die)
        CollisionComponent pcc = Mappers.collisionMapper.get(player);
        pcc.collisions.remove(bullet);
    }


}
