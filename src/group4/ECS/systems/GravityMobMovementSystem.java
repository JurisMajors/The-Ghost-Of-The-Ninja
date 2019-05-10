package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class GravityMobMovementSystem extends IteratingSystem {

    public GravityMobMovementSystem() {
        super(Families.movingGravityMobFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = Mappers.positionMapper.get(entity);
        MovementComponent movementComponent = Mappers.movementMapper.get(entity);
        GravityComponent gravityComponent = Mappers.gravityMapper.get(entity);
        PositionComponent playerPositionComponent = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));

        if(positionComponent.position.x<playerPositionComponent.position.x) movementComponent.velocity.x=Math.min(movementComponent.velocityRange.x,movementComponent.velocity.x+movementComponent.acceleration.x);
        if(positionComponent.position.x>playerPositionComponent.position.x) movementComponent.velocity.x=Math.max(-movementComponent.velocityRange.x,movementComponent.velocity.x-movementComponent.acceleration.x);
        movementComponent.velocity.y-=gravityComponent.gravity.y;

        boolean below=false;
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.allFamily);
        for(int i=0;i<entities.size();i++){
            PositionComponent entityPositionComponent = Mappers.positionMapper.get(entities.get(i));
            if(positionComponent.position.y-entityPositionComponent.position.y==entityPositionComponent.dimension.y+positionComponent.dimension.y
                    &&Math.abs(entityPositionComponent.position.x-positionComponent.position.x)<entityPositionComponent.dimension.x+positionComponent.dimension.x){
                below=true;
                break;
            }
        }
        if(below&&movementComponent.velocity.y<0)movementComponent.velocity.y=0;
        if(positionComponent.position.y<playerPositionComponent.position.y) movementComponent.velocity.y=Math.min(movementComponent.velocityRange.y,movementComponent.velocity.y+movementComponent.acceleration.y);
        positionComponent.position.addi(movementComponent.velocity.scale(deltaTime));
    }
}
