package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.DamageArea;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionData;

import java.util.Set;

public class MeleeCollision extends AbstractCollisionHandler<DamageArea> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new MeleeCollision();

    @Override
    public void collision(DamageArea dmgArea, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;

        for (CollisionData cd : others) {
            Entity other = cd.entity;

            if (other instanceof Mob) {
                handleMob(dmgArea, (Mob) other);
            }
        }

        // damage dealt by the area is dealt on a single tick
        TheEngine.getInstance().removeEntity(dmgArea);
    }

    private void handleMob(DamageArea dmgArea, Mob mob) {
        HealthComponent h = mob.getComponent(HealthComponent.class);
        DamageComponent dmg = dmgArea.getComponent(DamageComponent.class);

        // dealDmg
        h.health -= dmg.damage;
    }

    public static AbstractCollisionHandler getInstance(){
        return me;
    }
}
