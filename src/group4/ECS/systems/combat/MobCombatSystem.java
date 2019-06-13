package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.ItemComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.damage.DamageArea;
import group4.ECS.entities.mobs.FlyingMob;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class MobCombatSystem extends IteratingSystem {

    public MobCombatSystem(int priority) {
        super(Families.mobFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // whatever for now
        if (entity instanceof FlyingMob) return;

        MeleeWeaponComponent mwc = Mappers.meleeWeaponMapper.get(((Mob) entity).wpn);
        MobComponent mobc = Mappers.mobMapper.get(entity);
        RangeWeaponComponent rwc = Mappers.rangeWeaponMapper.get(((Mob) entity).wpn);

        // tick down cooldowns
        cooldown(deltaTime, (Mob) entity);

        if (mobc.state.equals(EntityConst.MobState.MELEE)) {
            // check cooldown
            if (mwc.currCooldown <= 0.0f) {
                mwc.currCooldown = mwc.cooldown;

                // attack melee
                meleeAttack((Mob) entity);
            }
        } else if (mobc.state.equals(EntityConst.MobState.RANGED)) {
                // TODO: ranged
        }
    }

    private void meleeAttack(Mob mob) {
        MeleeWeaponComponent mwc = Mappers.meleeWeaponMapper.get(mob.wpn);
        DimensionComponent dc = Mappers.dimensionMapper.get(mob);
        PositionComponent pc = Mappers.positionMapper.get(mob);
        PositionComponent ppc = Mappers.positionMapper.get(mob.level.getPlayer());

        if (mwc != null) {


            // hitbox offsets
            Vector3f trueOffset = new Vector3f(mwc.hitboxOffset);
            Vector3f trueHitbox = new Vector3f(mwc.hitBox);
            if (ppc.position.x < pc.position.x) {       // player is left of mob
                trueOffset.x = -1 * (mwc.hitboxOffset.x + mwc.hitBox.x);
            } else {
                trueOffset.x += dc.dimension.x;
            }

            trueOffset.addi(pc.position);

            // ignore ghost and all mobs
            Set<Class<? extends Entity>> excluded = new HashSet<>();
            excluded.add(Mob.class);
            excluded.add(Ghost.class);

            new DamageArea(trueOffset, trueHitbox,
                    mwc.damage, excluded, 0, mob);
        }

    }

    private void rangedAttack() {
        // TODO
    }

    private void cooldown(float deltaTime, Mob mob) {
        // hacky way to get the super component to itemcomponent
        ItemComponent ic = null;
        for (Component c : mob.wpn.getComponents()) {
            if (c instanceof ItemComponent) {
                ic = (ItemComponent) c;
            }
        }

        // if item slot used, but not item, throw exception
        if (ic == null) {
            throw new IllegalStateException("Mob holds invalid weapon: " + mob.wpn.getClass());
        }

        // update cooldown
        if (ic.currCooldown >= 0.0f) {
            ic.currCooldown -= deltaTime;
        }
    }

}

