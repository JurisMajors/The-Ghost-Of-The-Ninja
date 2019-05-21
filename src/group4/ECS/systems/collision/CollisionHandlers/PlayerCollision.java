package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;

import java.util.Set;


/**
 * An example of how we might want to handle collisions per Entity
 */
public class PlayerCollision extends AbstractCollisionHandler<Player> {

    private static AbstractCollisionHandler me = new PlayerCollision();

    @Override
    public void collision(Player player, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;
        // loop through all collisions and handle them accordingly
        for (CollisionData cd : others) {
            Entity other = cd.entity;
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
        HealthComponent h = player.getComponent(HealthComponent.class);
        DamageComponent dmg = bullet.getComponent(DamageComponent.class);
        // take damage
        h.health -= dmg.damage;
        // TODO: process knockback
        // after player bullet interaction we dont want to fix their positions (because the bullet might die)
        CollisionComponent pcc = Mappers.collisionMapper.get(player);
        pcc.collisions.remove(bullet);
    }


}
