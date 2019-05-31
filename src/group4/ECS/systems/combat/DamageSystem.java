package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;

public class DamageSystem extends IteratingSystem {

    public DamageSystem(int priority) {
        super(Families.dmgInflictingFamily, priority);
    }

    /**
     * This will resolve damage for all collisions of damage inflicting entities
     *
     * @param entity the entity which inflicts damage
     * @param deltaTime the time which passed in between the last and current tick
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent cc = Mappers.collisionMapper.get(entity);
        DamageComponent dc = Mappers.damageMapper.get(entity);

        // for all collisions of dmg inflicting entity
        for (CollisionData cd : cc.collisions) {

            // skip if entity is listed in set excluded entities
            if (dc.excluded.contains(cd.entity.getClass())) {
                continue;
            }

            // if colliding entity does not have health, skip
            if (!Mappers.healthMapper.has(cd.entity)) {
                continue;
            }

            int health = Mappers.healthMapper.get(cd.entity).health;

            // if entity dead, skip
            if (health <= 0) {
                continue;
            }

            // resolve damage
            Mappers.healthMapper.get(cd.entity).health = health - dc.damage;

            System.out.println(cd.entity);
            System.out.println(Mappers.healthMapper.get(cd.entity).health);
        }
    }

}

