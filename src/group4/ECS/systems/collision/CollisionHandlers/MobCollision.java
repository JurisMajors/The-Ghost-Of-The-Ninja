package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.entities.DamageArea;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.systems.collision.CollisionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MobCollision extends AbstractCollisionHandler<Mob> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new MobCollision();

    @Override
    public void collision(Mob e, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;
        List<CollisionData> removables = new ArrayList<>();
        // loop through all collisions and handle them accordingly
        for (CollisionData cd : others) {
            Entity other = cd.entity;
            // no displacement with dmg areas
            if (other instanceof DamageArea) {
                removables.add(cd);
            }
        }

        // remove entities for which we don't want displacement
        for (CollisionData data : removables) {
            others.remove(data);
        }
    }

    public static AbstractCollisionHandler getInstance(){
        return me;
    }

}
