package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.events.Event;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.Player;
import group4.ECS.entities.damage.DamageArea;
import group4.ECS.entities.hazards.Spikes;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;
import group4.maths.Vector3f;

import java.util.Set;

import static group4.ECS.components.stats.MovementComponent.LEFT;

public class DamageCollision extends AbstractCollisionHandler<Entity> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new DamageCollision();

    @Override
    public void collision(Entity e, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;

        outerloop:
        for (CollisionData cd : others) {
            Entity other = cd.entity;

            // if entity is excluded from damage, skip
            for (Class c : Mappers.damageMapper.get(e).excluded) {
                if (c.isInstance(other)) continue outerloop;
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

        // if entity immune to dmg, skip
        if (hc.state.contains(EntityConst.EntityState.IMMUNE)) {
            return;
        }

        // deal Dmg
        hc.health -= dmg.damage;

        if (dmg.origin instanceof Spikes) {
            // create temporary event to declare the entity to be immune to damage
            Event immune = new Event(other, 20,
                    (subject, dur, passed) -> {
                        if (passed == 0) {
                            Mappers.healthMapper.get(subject).state.add(EntityConst.EntityState.IMMUNE);
                            return;
                        }

                        if (passed >= dur) {
                            Mappers.healthMapper.get(subject).state.remove(EntityConst.EntityState.IMMUNE);
                        }
                    });
            immune.invoke();
        }

    }

    private void handleKnockback(Entity entity, Entity other) {
        MovementComponent mc = Mappers.movementMapper.get(other);
        HealthComponent hc = Mappers.healthMapper.get(other);

        // if not a moving object, don't apply knockback
        if (mc == null) {
            return;
        }

        // TODO: should be for all hazardous entities
        // behaviour of hazardous entities
        if (entity instanceof Spikes) {

            // resolve knockback only if not already knocked back (might not be necessary)
            if (hc.state.contains(EntityConst.EntityState.KNOCKED)) {
                return;
            }

            // min knockback velocity
            float knockback_vel = 0.3f;

            // if incoming velocity too low, set outgoing vector to minimum knockback
            float knock_factor = knockback_vel / mc.velocity.length();

            Vector3f n = ((Spikes) entity).normal;
            if (mc.velocity.x != 0.0f) {
                // compute reflection vector
                Vector3f d = mc.velocity;
                Vector3f r = d.sub(n.scale((2 * d.dot(n))));
                mc.velocity = r.scale(knock_factor);
            } else {
                if (n.equals(new Vector3f(0.0f, 1.0f, 0.0f))) {
                    mc.velocity = new Vector3f(0.0f, knockback_vel, 0.0f);
                } else if (n.equals(new Vector3f(0.0f, -1.0f, 0.0f))) {
                    mc.velocity = new Vector3f(0.0f, -knockback_vel, 0.0f);
                } else if (n.equals(new Vector3f(1.0f, 0.0f, 0.0f))) {
                    mc.velocity = new Vector3f(knockback_vel, 0.0f, 0.0f);
                } else {
                    mc.velocity = new Vector3f(-knockback_vel, 0.0f, 0.0f);
                }
            }

            // create a temporary event for indicating the entity to be knocked back
            Event knockback = new Event(other, 10,
                    (subject, dur, passed) -> {
                        if (passed == 0) Mappers.healthMapper.get(subject).state.add(EntityConst.EntityState.KNOCKED);
                        if (passed >= dur) Mappers.healthMapper.get(subject).state.remove(EntityConst.EntityState.KNOCKED);
                    });

            // trigger event
            knockback.invoke();
        }

        if (entity instanceof DamageArea) {
            DamageComponent wc = Mappers.damageMapper.get(entity);

            if (wc.origin instanceof Player) {
                MeleeWeaponComponent mwc = Mappers.meleeWeaponMapper.get(other);
                MovementComponent mc_player = Mappers.movementMapper.get(wc.origin);

                // TODO: meleeweapon should define how much kickback
                Vector3f knockback_vec = new Vector3f(0.2f, 0.1f, 0.0f);
                if (mc_player.orientation == LEFT) {
                    knockback_vec.x *= -1;
                }

                // displace
                mc.velocity.addi(knockback_vec);

                // knockback event
                Event knockback = new Event(other, 13, (subject, dur, passed) -> {
                    if (passed == 0) Mappers.healthMapper.get(subject).state.add(EntityConst.EntityState.KNOCKED);
                    if (passed >= dur) Mappers.healthMapper.get(subject).state.remove(EntityConst.EntityState.KNOCKED);
                });

                // trigger event
                knockback.invoke();
            }
        }
    }

    /**
     * @return singleton instance of this collision handler
     */
    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
