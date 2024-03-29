package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.CoinComponent;
import group4.ECS.components.identities.ConsumableComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.ScoreComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.bullets.Projectile;
import group4.ECS.entities.damage.DamageArea;
import group4.ECS.entities.Player;
import group4.ECS.entities.items.consumables.Coin;
import group4.ECS.entities.items.consumables.HealthPotion;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.entities.totems.StartTotem;
import group4.ECS.entities.totems.Totem;
import group4.ECS.entities.totems.TotemHelp;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionData;
import group4.audio.Sound;
import group4.maths.Vector3f;

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

        resetTotem(player);

        // loop through all collisions and handle them accordingly
        for (CollisionData cd : others) {
            Entity other = cd.entity;

            // example
            if (other instanceof Mob) {
                handleMob(player, (Mob) other);
            } else if (other instanceof Projectile) {
//                handleBullet(player, (Projectile) other);
                // after player bullet interaction we dont want to fix their positions (because the bullet might die)
                removables.add(cd);
            } else if (other instanceof Exit) {
                handleExit(player, (Exit) other);
                // after player exit interaction we dont want to fix their positions (we are just going to execute the exit action)
                removables.add(cd);
            } else if (other instanceof DamageArea) {
                removables.add(cd);
            } else if (other instanceof Coin) {
                if (!(player instanceof Ghost)) handleCoin(player, (Coin) other);
                removables.add(cd);
            } else if (other instanceof Totem) {
                if (player instanceof Ghost) {
                    ghostHandleTotem((Ghost) player, (Totem) other);
                } else {
                    playerHandleTotem(player, (Totem) other);
                }
                removables.add(cd);
            } else if (other instanceof HealthPotion) {
                handlePotion(player, (HealthPotion) other);
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

    private void handleCoin(Player player, Coin c) {
        ScoreComponent playerScore = Mappers.scoreMapper.get(player);
        CoinComponent coinComponent = Mappers.coinMapper.get(c);

        // update score
        playerScore.addScore(coinComponent.value);
        Sound.COIN.play();

        // remove coin from the module and the engine
        TheEngine.getInstance().removeEntity(c);
        player.level.getCurrentModule().removeEntity(c);
    }

    private void handlePotion(Player player, HealthPotion potion) {
        HealthComponent hc = Mappers.healthMapper.get(player);
        ConsumableComponent cc = Mappers.consumableMapper.get(potion);

        if (hc.health == hc.initialHealth) {
            return;
        }

        if (cc.effectID == EntityConst.EffectID.SMALL_HEAL) {
            hc.health += 30;
        } else if (cc.effectID == EntityConst.EffectID.MEDIUM_HEAL) {
            hc.health += 60;
        } else if (cc.effectID == EntityConst.EffectID.LARGE_HEAL) {
            hc.health += 100;
        }

        if (hc.health > hc.initialHealth) {
            hc.health = hc.initialHealth;
        }

        TheEngine.getInstance().removeEntity(potion);
        player.level.getCurrentModule().removeEntity(potion);
    }

    private void resetTotem(Player player) {
        player.totemStatus = null; // reset totem status
        TheEngine.getInstance().removeEntity(TotemHelp.getInstance()); // remove help
    }

    private void playerHandleTotem(Player player, Totem totem) {
        if (totem.isEnd()) {
            if (player.spawnedGhost && player.challanging && player.startTotemID == totem.getID()) { // ghost is still alive
                // means that we got the there faster
                player.getComponent(ScoreComponent.class).addScore(Totem.CHALLANGEREWARD);
                player.challanging = false;
            }
            // end totem, player cannot do a lot here
        } else {
            // starting totem
            if (player.spawnedGhost) return; // if a ghost is alive dont do shit
            player.totemStatus = (StartTotem) totem;
            // otherwise player is on starting totem and he need to see the help image
            Vector3f helpPos = new Vector3f(totem.getComponent(PositionComponent.class).position);
            helpPos.y += totem.getComponent(DimensionComponent.class).dimension.y;
            helpPos.x += totem.getComponent(DimensionComponent.class).dimension.x * 0.1f;
            TheEngine.getInstance().addEntity(TotemHelp.getHelp(helpPos));
        }
    }

    private void ghostHandleTotem(Ghost ghost, Totem totem) {
        if (!totem.isEnd() || ghost.endTotem != totem.getID()) return;
        Vector3f gPos = ghost.getComponent(PositionComponent.class).position;
        Vector3f tPos = totem.getComponent(PositionComponent.class).position;
        Vector3f tDim = totem.getComponent(DimensionComponent.class).dimension;
        if (gPos.x >= tPos.add(tDim.scale(0.3f)).x) {
            ghost.getComponent(HealthComponent.class).health = 0;
            GraphicsComponent.clearGlobalColorMask();
        }
    }

    private static void handleMob(Player player, Mob mob) {
        // this is a placeholder to show how the system would work
    }

//    private static void handleBullet(Player player, Projectile bullet) {
//        HealthComponent h = player.getComponent(HealthComponent.class);
//        DamageComponent dmg = bullet.getComponent(DamageComponent.class);
//
//        // take damage
//        h.health -= dmg.damage;
//        // TODO: process knockback
//    }

    private static void handleExit(Player player, Exit exit) {
        GraphicsComponent.clearGlobalColorMask();
        if (player instanceof Ghost) { // kill ghost if has reached exit
            player.getComponent(HealthComponent.class).health = 0;
            GraphicsComponent.clearGlobalColorMask();
        } else {
            exit.module.getLevel().handleExit(exit);
        }
    }


}
