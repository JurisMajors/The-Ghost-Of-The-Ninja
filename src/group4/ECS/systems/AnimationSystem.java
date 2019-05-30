package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.etc.Families;
import group4.game.IKEndEffector;
import group4.maths.Vector3f;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() { super(Families.animationFamily); }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity instanceof HierarchicalPlayer) {
            for (IKEndEffector handle : ((HierarchicalPlayer) entity).IKHandles) {
                handle.t += deltaTime / 200;
                handle.t %= 2.0f * Math.PI; // Rotate in radians.

                Vector3f unitCircle;

                if (handle.label == "foot_L") {
                    unitCircle = new Vector3f((float) Math.cos(handle.t) * 0.75f, (float) Math.sin(handle.t) * 0.75f, 0.0f);
                } else { // "foot_R"
                    unitCircle = new Vector3f((float) Math.cos(handle.t) * 0.9f, (float) Math.sin(handle.t) * 0.9f, 0.0f);
                }

                // Scale slightly inwards to fix flashing issue (caused by arccos(x) where x>1)
                unitCircle.scalei(0.99f);
                
                // Move handle.endPos to be relative of the player position
                handle.endPos = unitCircle.add(new Vector3f(entity.getComponent(DimensionComponent.class).dimension.x / 2, 0.8f, 0.0f));

                // Get the length of the body parts involved in the IK system
                float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
                float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;

                // Calculate and set the new rotations for the limbs based on the start and endpoints of the IK chain
                float[] angles = ((HierarchicalPlayer) entity).getLimbAngles(handle.startPos, handle.endPos, upperLength, lowerLength);
                handle.upper.rotation = angles[0];
                handle.lower.rotation = angles[1];
            }
        }
    }
}
