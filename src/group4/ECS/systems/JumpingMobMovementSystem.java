package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public class JumpingMobMovementSystem extends MobMovementSystem {

    public JumpingMobMovementSystem() {
        super(Families.jumpingMobFamily);
    }

    @Override
    protected boolean canMoveLeft(Vector3f velocity) {
        super.canMoveLeft(velocity);
        return !(velocity.y <= 1e-3 && velocity.y >= -1e-3);
    }

    @Override
    protected boolean canMoveRight(Vector3f velocity) {
        super.canMoveRight(velocity);
        return !(velocity.y <= 1e-3 && velocity.y >= -1e-3);
    }

    @Override
    protected void move(Entity e, PositionComponent playerPos, float deltaTime) {
        super.move(e, playerPos, deltaTime);
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        // set velocity in the direction that keyboard asks for
        if (pc.position.y < playerPos.position.y && canJump(mc.velocity)) {
            jump(mc);
        } else if (pc.position.x < playerPos.position.x && !canMoveRight(mc.velocity) && canJump(mc.velocity)) {
            jump(mc);
            moveRight(mc);
        } else if (pc.position.x < playerPos.position.x && canMoveRight(mc.velocity)) {
            moveRight(mc);
        } else if (pc.position.x > playerPos.position.x && !canMoveLeft(mc.velocity) && canJump(mc.velocity)) {
            jump(mc);
            moveLeft(mc);
        } else if (pc.position.x > playerPos.position.x && canMoveLeft(mc.velocity)) {
            moveLeft(mc);
        } else {
            // stay still if no keys are pressed
            mc.velocity.x = 0;
        }
    }
}
