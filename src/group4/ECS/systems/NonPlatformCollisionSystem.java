package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class NonPlatformCollisionSystem extends IteratingSystem {

    public NonPlatformCollisionSystem() {
        super(Families.movingNonPlatformFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = Mappers.positionMapper.get(entity);
        MovementComponent movementComponent = Mappers.movementMapper.get(entity);

        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.movingNonPlatformFamily);
        for(int i=0;i<entities.size();i++){
            PositionComponent entityPositionComponent = Mappers.positionMapper.get(entities.get(i));
            MovementComponent entityMovementComponent = Mappers.movementMapper.get(entities.get(i));
            //not the same entities
            if(positionComponent.position.x==entityPositionComponent.position.x&&positionComponent.position.y==entityPositionComponent.position.y&&positionComponent.position.z==entityPositionComponent.position.z)continue;

            if(Math.abs(entityPositionComponent.position.x-positionComponent.position.x)<entityPositionComponent.dimension.x+positionComponent.dimension.x
            &&Math.abs(entityPositionComponent.position.y-positionComponent.position.y)<entityPositionComponent.dimension.y+positionComponent.dimension.y){
                if(entityPositionComponent.dimension.x+positionComponent.dimension.x-Math.abs(entityPositionComponent.position.x-positionComponent.position.x)
                        <entityPositionComponent.dimension.y+positionComponent.dimension.y-Math.abs(entityPositionComponent.position.y-positionComponent.position.y)){
                    if(entityPositionComponent.position.x-positionComponent.position.x>0){//move right
                        entityPositionComponent.position.x=positionComponent.position.x+entityPositionComponent.dimension.x+positionComponent.dimension.x;
                        if(entityMovementComponent.velocity.x<0)entityMovementComponent.velocity.x=0;
                    }
                    else{//move left
                        entityPositionComponent.position.x=positionComponent.position.x-entityPositionComponent.dimension.x-positionComponent.dimension.x;
                        if(entityMovementComponent.velocity.x>0)entityMovementComponent.velocity.x=0;
                    }
                }
                else{
                    if(entityPositionComponent.position.y-positionComponent.position.y>0){//move up
                        entityPositionComponent.position.y=positionComponent.position.y+entityPositionComponent.dimension.y+positionComponent.dimension.y;
                        if(entityMovementComponent.velocity.y<0)entityMovementComponent.velocity.y=0;
                    }
                    else{//move down
                        entityPositionComponent.position.y=positionComponent.position.y-entityPositionComponent.dimension.y-positionComponent.dimension.y;
                        if(entityMovementComponent.velocity.y>0)entityMovementComponent.velocity.y=0;
                    }
                }
            }
        }
    }
}
