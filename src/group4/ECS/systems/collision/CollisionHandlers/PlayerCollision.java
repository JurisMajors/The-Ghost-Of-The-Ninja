package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.DamageArea;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.entities.world.Exit;
import group4.ECS.entities.world.Platform;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionData;
import group4.game.Main;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
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

            // Ghost on platform/spline detection (while not training)
            if (player instanceof Ghost && (other instanceof Platform || Families.collidableSplineFamily.matches(other)) && !Main.AI) {
                Entity mainCamera = TheEngine.getInstance().getEntitiesFor(Families.cameraFamily).get(0);

                // Check whether the ghost is off screen
                Vector3f mainCameraPosition = mainCamera.getComponent(PositionComponent.class).position;
                Vector3f ghostPosition = player.getComponent(PositionComponent.class).position;

                if (!(ghostPosition.x <= mainCameraPosition.x + Main.SCREEN_WIDTH / 2 && ghostPosition.x >= mainCameraPosition.x - Main.SCREEN_WIDTH / 2
                    && ghostPosition.y <= mainCameraPosition.y + Main.SCREEN_HEIGHT / 2 && ghostPosition.y >= mainCameraPosition.y - Main.SCREEN_HEIGHT / 2)) {
                    // Ghost is on a platform and not visible on the screen of the user, so let it wait
                    ((Ghost) player).setBlocked(true);
                } else {
                    // Ghost is on a platform and visible on the screen of the user, check if is is blocked
                    if (((Ghost) player).isBlocked()) {
                        // Ghost is indeed blocked, wait until it is sufficiently visible on the screen and then continue moving the ghost
                        Vector3f ghostDimension = player.getComponent(DimensionComponent.class).dimension;
                        if (ghostPosition.x <= mainCameraPosition.x + Main.SCREEN_WIDTH / 2 - 2 * ghostDimension.x) {
                            ((Ghost) player).setBlocked(false);
                        }
                    }
                }

            }

            // example
            if (other instanceof Mob) {
                handleMob(player, (Mob) other);
            } else if (other instanceof Bullet) {
//                handleBullet(player, (Bullet) other);
                // after player bullet interaction we dont want to fix their positions (because the bullet might die)
                removables.add(cd);
            } else if (other instanceof Exit) {
                handleExit(player, (Exit) other);
                // after player exit interaction we dont want to fix their positions (we are just going to execute the exit action)
                removables.add(cd);
            } else if (other instanceof DamageArea) { // TODO: super janky, damageArea does not apply to player whatsoever
                removables.add(cd);
            }
        }

        // remove entities for which we don't want displacement
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

//    private static void handleBullet(Player player, Bullet bullet) {
//        HealthComponent h = player.getComponent(HealthComponent.class);
//        DamageComponent dmg = bullet.getComponent(DamageComponent.class);
//
//        // take damage
//        h.health -= dmg.damage;
//        // TODO: process knockback
//    }

    private static void handleExit(Player player, Exit exit) {
        if (Main.AI && player instanceof Ghost) { // kill ghost if has reached exit
            player.getComponent(HealthComponent.class).health = 0;
        } else if (player instanceof  Ghost) {
            return;
        }
        exit.module.getLevel().handleExit(exit);
    }


}
