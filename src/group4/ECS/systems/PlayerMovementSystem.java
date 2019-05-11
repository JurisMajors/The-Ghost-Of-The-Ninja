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
        GravityComponent gc = Mappers.gravityMapper.get(entity);

        //accelerate on the x coordinate according to keyboard keys
        if (KeyBoard.isKeyDown(GLFW_KEY_D) && !KeyBoard.isKeyDown(GLFW_KEY_A))
            mc.velocity.x = Math.min(mc.velocityRange.x, mc.velocity.x + mc.acceleration.x);
        else if (KeyBoard.isKeyDown(GLFW_KEY_A) && !KeyBoard.isKeyDown(GLFW_KEY_D))
            mc.velocity.x = Math.max(-mc.velocityRange.x, mc.velocity.x - mc.acceleration.x);
        else if (mc.velocity.x != 0) mc.velocity.x -= mc.velocity.x / Math.abs(mc.velocity.x) * mc.acceleration.x;

        //take gravity into account
        mc.velocity.y -= gc.gravity.y;

        boolean below = false;
        //all entities (with a position component)
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntitiesFor(Families.allFamily);
        for (int i = 0; i < entities.size(); i++)
            if (entity != entities.get(i)) {
                //position component of entity
                PositionComponent epc = Mappers.positionMapper.get(entities.get(i));
                if (epc.position.y + epc.dimension.y == pc.position.y && pc.position.x <= epc.position.x + epc.dimension.x && pc.position.x + pc.dimension.x >= epc.position.x) {
                    below = true;
                    break;
                }
            }

        //if the entity is standing right on top of any other entity
        if (below) {
            //no downward velocity
            if (mc.velocity.y < 0) mc.velocity.y = 0;
            //accelerate on the y coordinate according to keyboard keys
            if (KeyBoard.isKeyDown(GLFW_KEY_W) && !KeyBoard.isKeyDown(GLFW_KEY_S))
                mc.velocity.y = Math.min(mc.velocityRange.y, mc.velocity.y + mc.acceleration.y);
        }

        //set position of lbb corner of entity after movement
        pc.position.addi(mc.velocity.scale(deltaTime));
    }
}