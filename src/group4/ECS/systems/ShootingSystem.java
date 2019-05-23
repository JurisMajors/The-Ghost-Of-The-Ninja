package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.bullets.Bullet;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class ShootingSystem extends IteratingSystem {

    public ShootingSystem() {
        super(Families.shootingFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        RangeWeaponComponent sc = Mappers.shootingMapper.get(entity);
        PositionComponent playerPos = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        if (sc.wait < sc.rate) sc.wait++; //count frames until next shot
        else { //shoot
            sc.wait = 0;
            //create new bullet
            TheEngine.getInstance().addEntity(new Bullet(pc.position.add(sc.position), playerPos.position.sub(pc.position.add(sc.position)).normalized()));
        }
    }
}
