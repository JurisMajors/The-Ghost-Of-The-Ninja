package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;

public class DamageSystem extends IteratingSystem {

    public DamageSystem() { super(Families.dmgInflictingFamily); }

    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent cc = Mappers.collisionMapper.get(entity);
        DamageComponent dc = Mappers.damageMapper.get(entity);

        for (CollisionData cd : cc.collisions) {
            if (!Mappers.healthMapper.has(cd.entity)) {
                continue;
            }

            int health = Mappers.healthMapper.get(cd.entity).health;
            System.out.println(health);

            if (health <= 0) {
                break;
            }

            Mappers.healthMapper.get(cd.entity).health = health - dc.damage;
        }
    }

}

