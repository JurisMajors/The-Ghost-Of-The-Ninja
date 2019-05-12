package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.GravityComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.input.KeyBoard;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerMovementSystem extends IteratingSystem {

    public PlayerMovementSystem() {
        super(Families.playerFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);

        processGravity(entity, deltaTime);
        move(entity, deltaTime);

        //set position of lbb corner of entity after movement
        pc.position.addi(mc.velocity.scale(deltaTime));
    }

    protected void move(Entity e, float deltaTime) {
        MovementComponent mc = Mappers.movementMapper.get(e);
        //accelerate on the x coordinate according to keyboard keys
        if (KeyBoard.isKeyDown(GLFW_KEY_D)) {
            moveRight(mc);
        } else if (KeyBoard.isKeyDown(GLFW_KEY_A)) {
            moveLeft(mc);
        } else {
            mc.velocity.x = 0;
        }
        // jumping
        if (KeyBoard.isKeyDown(GLFW_KEY_SPACE) && mc.velocity.y == 0) {
            jump(mc);
        }
    }

    private void moveRight(MovementComponent mc) {
        //mc.velocity.x = Math.min(mc.velocityRange.x,
        //mc.velocity.x + mc.acceleration.x);
        mc.velocity.x = mc.velocityRange.x;
    }

    private void moveLeft(MovementComponent mc) {
        mc.velocity.x = -1 * mc.velocityRange.x;
    }

    private void jump(MovementComponent mc) {
        mc.velocity.y = mc.velocityRange.y;
    }

    protected void processGravity(Entity e, float deltaTime) {
        GravityComponent gc = Mappers.gravityMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        //take gravity into account
        mc.velocity.y -= gc.gravity.y;

        //if the entity is standing right on top of any other entity
        if (MobMovementSystem.hasBelow(e)) {
            //no downward velocity
            if (mc.velocity.y < 0) {
                mc.velocity.y = 0;
            }
            //accelerate on the y coordinate according to keyboard keys
            if (KeyBoard.isKeyDown(GLFW_KEY_W) && !KeyBoard.isKeyDown(GLFW_KEY_S)) {
                mc.velocity.y = Math.min(mc.velocityRange.y, mc.velocity.y + mc.acceleration.y);
            }
        }
    }

}
