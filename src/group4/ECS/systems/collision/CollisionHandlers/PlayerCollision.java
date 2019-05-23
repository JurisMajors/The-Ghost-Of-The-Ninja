package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;
import group4.game.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * An example of how we might want to handle collisions per Entity
 */
public class PlayerCollision extends AbstractCollisionHandler<Player> {

    private static AbstractCollisionHandler me = new PlayerCollision();

    @Override
    public void collision(Player player, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;
        List<CollisionData> removables = new ArrayList<>();
        // loop through all collisions and handle them accordingly
        for (CollisionData cd : others) {
            Entity other = cd.entity;
            // example
            if (other instanceof Mob) {
                handleMob(player, (Mob) other);
            } else if (other instanceof Bullet) {
                handleBullet(player, (Bullet) other);
                // after player bullet interaction we dont want to fix their positions (because the bullet might die)
                removables.add(cd);
            } else if (other instanceof Exit) {
                handleExit(player, (Exit) other);
                // after player exit interaction we dont want to fix their positions (we are just going to execute the exit action)
                removables.add(cd);
            }
        }
        for (CollisionData data : removables) {
            others.remove(data);
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
    }

    private static void handleExit(Player player, Exit exit) {
        if (Main.AI && player instanceof Ghost) { // kill ghost if has reached exit
            player.getComponent(HealthComponent.class).health = 0;
        } else if (player instanceof  Ghost) {
            return;
        }
        exit.module.getLevel().handleExit(exit);
    }


}
