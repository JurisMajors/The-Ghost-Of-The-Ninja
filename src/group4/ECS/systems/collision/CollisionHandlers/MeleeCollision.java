package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionData;
import group4.maths.Vector3f;

import java.util.Set;

public class MeleeCollision extends AbstractCollisionHandler<Entity> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new MeleeCollision();

    @Override
    public void collision(Entity e, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;

        for (CollisionData cd : others) {
            Entity other = cd.entity;

            // if entity is excluded from damage, skip
            if (Mappers.damageMapper.get(e).excluded.contains(other.getClass())) {
                continue;
            }

            // compute damage and knockback
            if (Mappers.healthMapper.get(other) != null) {
                handleDmg(e, other);
                handleKnockback(e, other);
            }
        }

        // clear collisions
        others.clear();
    }

    private void handleDmg(Entity entity, Entity other) {
        HealthComponent hc = other.getComponent(HealthComponent.class);
        DamageComponent dmg = entity.getComponent(DamageComponent.class);

        System.out.println(hc.health);

        // deal Dmg
        hc.health -= dmg.damage;
    }

    private void handleKnockback(Entity entity, Entity other) {
        MovementComponent mc = Mappers.movementMapper.get(other);

        System.out.println("here");

        // min knockback velocity
        float minKnockBack = 2.0f;

        // if moving object
        if (mc != null) {

            float boost = 1.0f;
            if (mc.velocity.length() < minKnockBack) {
                boost = minKnockBack / mc.velocity.length();
            }

            System.out.println(mc.velocity);
            mc.velocity = mc.velocity.scale(-boost);
        }
    }

    /**
     * @return singleton instance of this collision handler
     */
    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
