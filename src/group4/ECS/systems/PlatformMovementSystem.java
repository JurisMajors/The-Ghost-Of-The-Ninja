package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.PositionComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.RangeComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class PlatformMovementSystem extends IteratingSystem {

    public PlatformMovementSystem() {
        super(Families.movingPlatformFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = Mappers.positionMapper.get(entity);
        MovementComponent movementComponent = Mappers.movementMapper.get(entity);
        RangeComponent rangeComponent = Mappers.rangeMapper.get(entity);

        //movement in range 3d
        Vector3f position = positionComponent.position.add(movementComponent.velocity.scale(deltaTime));
        Vector3f pos = position.add(rangeComponent.lbbCorner.scale(-1.0f));
        boolean change=false;
        if(pos.x<0){
            positionComponent.position.x-=2*pos.x;
            position.x-=pos.x;
            change=true;
        }
        if(pos.y<0){
            positionComponent.position.y-=2*pos.y;
            position.y-=pos.y;
            change=true;
        }
        if(pos.z<0){
            positionComponent.position.z-=2*pos.z;
            position.z-=pos.z;
            change=true;
        }
        if(!change) {
            pos = rangeComponent.rtfCorner.add(position.scale(-1.0f));
            if (pos.x < 0) {
                positionComponent.position.x += 2 * pos.x;
                position.x += pos.x;
                change=true;
            }
            if (pos.y < 0) {
                positionComponent.position.y += 2 * pos.y;
                position.y += pos.y;
                change=true;
            }
            if (pos.z < 0) {
                positionComponent.position.z += 2 * pos.z;
                position.z += pos.z;
                change=true;
            }
        }
        if(change)movementComponent.velocity.scalei(-1.0f);
        positionComponent.position.addi(movementComponent.velocity.scale(deltaTime));

        /*//handle collisions with final position 2d
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.movingNonPlatformFamily);
        for(int i=0;i<entities.size();i++){
            PositionComponent entityPositionComponent = Mappers.positionMapper.get(entities.get(i));
            MovementComponent entityMovementComponent = Mappers.movementMapper.get(entities.get(i));
            if(Math.abs(entityPositionComponent.position.x-positionComponent.position.x)<entityPositionComponent.dimension.x+positionComponent.dimension.x
            &&Math.abs(entityPositionComponent.position.y-positionComponent.position.y)<entityPositionComponent.dimension.y+positionComponent.dimension.y){
                if(entityPositionComponent.dimension.x+positionComponent.dimension.x-Math.abs(entityPositionComponent.position.x-positionComponent.position.x)
                        <entityPositionComponent.dimension.y+positionComponent.dimension.y-Math.abs(entityPositionComponent.position.y-positionComponent.position.y)){
                    if(entityPositionComponent.position.x-positionComponent.position.x>0){//move right
                        entityPositionComponent.position.x=positionComponent.position.x+entityPositionComponent.dimension.x+positionComponent.dimension.x;
                    }
                    else{//move left
                        entityPositionComponent.position.x=positionComponent.position.x-entityPositionComponent.dimension.x-positionComponent.dimension.x;
                    }
                }
                else{
                    if(entityPositionComponent.position.y-positionComponent.position.y>0){//move up
                        entityPositionComponent.position.y=positionComponent.position.y+entityPositionComponent.dimension.y+positionComponent.dimension.y;
                    }
                    else{//move down
                        entityPositionComponent.position.y=positionComponent.position.y-entityPositionComponent.dimension.y-positionComponent.dimension.y;
                        veloci
                    }
                }
            }
        }*/
    }
}
