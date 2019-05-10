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
        boolean change = false;
        if (pos.x < 0) {
            positionComponent.position.x -= 2 * pos.x;
            position.x -= pos.x;
            change = true;
        }
        if (pos.y < 0) {
            positionComponent.position.y -= 2 * pos.y;
            position.y -= pos.y;
            change = true;
        }
        if (pos.z < 0) {
            positionComponent.position.z -= 2 * pos.z;
            position.z -= pos.z;
            change = true;
        }
        if (!change) {
            pos = rangeComponent.rtfCorner.add(position.scale(-1.0f));
            if (pos.x < 0) {
                positionComponent.position.x += 2 * pos.x;
                position.x += pos.x;
                change = true;
            }
            if (pos.y < 0) {
                positionComponent.position.y += 2 * pos.y;
                position.y += pos.y;
                change = true;
            }
            if (pos.z < 0) {
                positionComponent.position.z += 2 * pos.z;
                position.z += pos.z;
                change = true;
            }
        }
        if (change) movementComponent.velocity.scalei(-1.0f);
        positionComponent.position.addi(movementComponent.velocity.scale(deltaTime));

    }
}
