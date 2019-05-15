package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.components.ShootingComponent;
import group4.ECS.entities.items.weapons.Bullet;
import group4.ECS.entities.mobs.FlyingMob;
import group4.ECS.entities.mobs.MobBullet;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class ShootingSystem extends IteratingSystem {

    public ShootingSystem() {
        super(Families.shootingFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        ShootingComponent sc = Mappers.shootingMapper.get(entity);
        PositionComponent playerPos = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        if(sc.wait<sc.rate)sc.wait++;
        else{
            TheEngine.getInstance().addEntity(new MobBullet(pc.position.add(sc.position),playerPos.position.sub(pc.position.add(sc.position)).normalized()));
        }
    }
}
