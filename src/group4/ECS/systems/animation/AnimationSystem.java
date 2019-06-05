package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.etc.EntityState;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;
import group4.utils.DebugUtils;
import javafx.geometry.Pos;

public class AnimationSystem extends IteratingSystem {
    float deltaTime;

    public AnimationSystem(int priority) {
        super(Families.animationFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.deltaTime = deltaTime;

        if (entity instanceof HierarchicalPlayer) {
            animatePlayer((HierarchicalPlayer) entity);
        }
    }

    private void animatePlayer(HierarchicalPlayer player) {
        AnimationComponent ac = Mappers.animationMapper.get(player);

        // Update the active animation to match the current state of the player
        ac.setAnimation(player.getState());

        // Update the positions according to the animation that is currently active
        ac.getCurrentAnimation().update(this.deltaTime);

        // Temp thing to animate the hip bounce during walking
        player.setHipPosition(player.getTorso().getComponent(PositionComponent.class).position.add(player.hipOffset));
        player.getTorso().relativePosition = player.getHipPosition();

        // Handle the IK logic for determining final limb rotations. Animations only update key positions, such as those of the IKEndEffector(s)
        for (String handleName : player.IKHandles.keySet()){
            IKEndEffector handle = player.IKHandles.get(handleName);

            // Move handle.endPos to be relative of the player position
            PositionComponent pc = Mappers.positionMapper.get(handle);
            DebugUtils.drawCircle(pc.position.add(player.getComponent(PositionComponent.class).position).add(new Vector3f(player.getComponent(DimensionComponent.class).dimension.x / 2, 0.0f, 0.0f)), .05f, 30);
            pc.position = pc.position.add(new Vector3f(player.getComponent(DimensionComponent.class).dimension.x / 2, 0.0f, 0.0f));
            // Get the length of the body parts involved in the IK system
            float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
            float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;

            // Calculate and set the new rotations for the limbs based on the start and endpoints of the IK chain
            boolean bendForward = handleName.startsWith("foot");
            float[] angles = player.getLimbAngles(player.getHipPosition(), pc.position, upperLength, lowerLength, bendForward);
            handle.upper.rotation = angles[0];
            handle.lower.rotation = angles[1];
        }
        



    }
}