package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public abstract class MobMovementSystem extends IteratingSystem {

    public MobMovementSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        // get the player position
        PositionComponent playerPos = Mappers.positionMapper.get(TheEngine.getInstance().
                getEntitiesFor(Families.playerFamily).get(0));

        move(entity, playerPos, deltaTime);
        processGravity(entity, playerPos, deltaTime);

        pc.position.addi(mc.velocity.scale(deltaTime));

    }

    protected void move(Entity e, PositionComponent playerPos, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);

        if (pc.position.x < playerPos.position.x) {
            mc.velocity.x = Math.min(mc.velocityRange.x, mc.velocity.x + mc.acceleration.x);
        } else if (pc.position.x > playerPos.position.x) {
            mc.velocity.x = Math.max(-mc.velocityRange.x, mc.velocity.x - mc.acceleration.x);
        } else if (mc.velocity.x != 0) { //if entity has same x coordinate as player decrease (absolute value of) velocity.x
            mc.velocity.x -= mc.velocity.x / Math.abs(mc.velocity.x) * mc.acceleration.x;
        }
    }

    protected void processGravity(Entity e, PositionComponent playerPos, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        GravityComponent gc = Mappers.gravityMapper.get(e);

        //take gravity into account
        mc.velocity.y -= gc.gravity.y;

        // if nobody below continue processing movement
        if (!MobMovementSystem.hasBelow(e)) return;

        //no downward velocity
        if (mc.velocity.y < 0){
            mc.velocity.y = 0;
        }
        //accelerate on the y coordinate (jump) towards player
        if (pc.position.y < playerPos.position.y) {
            mc.velocity.y = Math.min(mc.velocityRange.y, mc.velocity.y + mc.acceleration.y);
        }
    }

    public static boolean hasBelow(Entity e) {
        PositionComponent pc = Mappers.positionMapper.get(e);
        DimensionComponent dc = Mappers.dimensionMapper.get(e);

        //all entities (with a position component)
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.collidableFamily);
        for (int i = 0; i < entities.size(); i++) {

            if (e != entities.get(i)) {
                //position component of entity
                PositionComponent epc = Mappers.positionMapper.get(entities.get(i));
                DimensionComponent edc = Mappers.dimensionMapper.get(entities.get(i));

                if (epc.position.y + edc.dimension.y >= pc.position.y &&
                        pc.position.x <= epc.position.x + edc.dimension.x &&
                        pc.position.x + dc.dimension.x >= epc.position.x) {
                    return true;
                }
            }
        }
        return false;
    }

}

