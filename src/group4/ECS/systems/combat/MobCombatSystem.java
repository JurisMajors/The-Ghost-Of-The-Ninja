package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
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

        MobComponent mobc = Mappers.mobMapper.get(entity);
        RangeWeaponComponent rwc = Mappers.rangeWeaponMapper.get(((Mob) entity).wpn);

        cooldown(deltaTime, entity);

        if (mobc.state.equals(EntityConst.MobState.MELEE)) {
            meleeAttack(entity);
        } else if(mobc.state.equals(EntityConst.MobState.RANGED)) {

        }
    }

    private void meleeAttack(Entity entity) {
        MeleeWeaponComponent mwc = Mappers.meleeWeaponMapper.get(((Mob) entity).wpn);
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        PositionComponent pc = Mappers.positionMapper.get(entity);
        PositionComponent ppc = Mappers.positionMapper.get(((Mob) entity).level.getPlayer());

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

//                System.out.println(trueOffset);
//                System.out.println(trueHitbox);

            new DamageArea(trueOffset, trueHitbox,
                    mwc.damage, excluded, 0, entity);
        }

    }

    private void rangedAttack() {

    }

    private void cooldown(float deltaTime, Entity entity) {

    }

}

