package group4.ECS.systems.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.CollisionComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.items.weapons.Bullet;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.Mappers;

import java.util.Collection;


/**
 * An example of how we might want to handle collisions per Entity
 */
public class PlayerCollision {

    /**
     * This function specifies how a player deals with different types of collisions.
     *
     * @param player the player
     * @param others list of entities the player is colliding with
     */
    public static void collision(Player player, Collection<Entity> others) {
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

    private static void handleMob(Player player, Mob mob) {
        // this is a placeholder to show how the system would work
    }

    private static void handleBullet(Player player, Bullet bullet) {

        // after player bullet interaction we dont want to fix their positions (because the bullet might die)
        CollisionComponent pcc = Mappers.collisionMapper.get(player);
        CollisionComponent bcc = Mappers.collisionMapper.get(bullet);
        pcc.collisions.remove(bullet);
        bcc.collisions.remove(player);
    }


}
