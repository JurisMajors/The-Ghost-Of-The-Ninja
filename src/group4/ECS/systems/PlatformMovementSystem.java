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
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        RangeComponent rc = Mappers.rangeMapper.get(entity);

        //set position of lbb corner of entity after movement without taking the range into account
        pc.position.addi(mc.velocity.scale(deltaTime));

        //vector from lbb corner of range to position
        Vector3f lbbdist = pc.position.sub(rc.lbbCorner);
        //vector from position to rtf corner of range
        Vector3f rtfdist = rc.rtfCorner.sub(pc.position);

        //if position of lbb corner is outside of the range compute where it should have ended up being when taking the range into account and change velocity to -velocity
        if (lbbdist.x < 0) {
            pc.position.x -= 2 * lbbdist.x;
            pc.position.y -= 2 * lbbdist.x * mc.velocity.y / mc.velocity.x;
            mc.velocity.scalei(-1.0f);
        } else if (lbbdist.y < 0) {
            pc.position.y -= 2 * lbbdist.y;
            pc.position.x -= 2 * lbbdist.y * mc.velocity.x / mc.velocity.y;
            mc.velocity.scalei(-1.0f);
        } else if (rtfdist.x < 0) {
            pc.position.x += 2 * rtfdist.x;
            pc.position.y += 2 * rtfdist.x * mc.velocity.y / mc.velocity.x;
            mc.velocity.scalei(-1.0f);
        } else if (rtfdist.y < 0) {
            pc.position.y += 2 * rtfdist.y;
            pc.position.x += 2 * rtfdist.y * mc.velocity.x / mc.velocity.y;
            mc.velocity.scalei(-1.0f);
        }
    }
}
