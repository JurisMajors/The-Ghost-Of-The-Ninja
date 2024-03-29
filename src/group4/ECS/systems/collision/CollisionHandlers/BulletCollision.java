package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.Splash;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.bullets.Projectile;
import group4.ECS.entities.world.Block;
import group4.ECS.entities.world.Platform;
import group4.ECS.entities.world.SplinePlatform;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionData;
import group4.graphics.Texture;

import java.util.Map;
import java.util.Set;

public class BulletCollision extends AbstractCollisionHandler<Projectile> {

    /**
     * Singleton
     **/
    private static AbstractCollisionHandler me = new BulletCollision();

    @Override
    public void collision(Projectile e, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;

        outerloop:
        for (CollisionData cd : others) {
            Entity other = cd.entity;

            // if entity is excluded from damage, skip
            for (Class c : Mappers.damageMapper.get(e).excluded) {
                if (c.isInstance(other)) continue outerloop;
            }

            // ghost is ignored in damage component of the projectile
            if (other instanceof Platform || other instanceof SplinePlatform || other instanceof Block) {
                // remove from engine
                new Splash(Mappers.positionMapper.get(e).position, 4);
                TheEngine.getInstance().removeEntity(e);
            } else if (other instanceof Player) {
                // deal damage
                Mappers.healthMapper.get(other).health -= Mappers.damageMapper.get(e).damage;
                new Splash(Mappers.positionMapper.get(e).position, 4);
                TheEngine.getInstance().removeEntity(e);
            }

            // TODO: play sound on creation and impact
        }
    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
